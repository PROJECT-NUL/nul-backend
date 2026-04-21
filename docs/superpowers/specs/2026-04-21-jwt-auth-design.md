# JWT 인증 시스템 설계

**날짜:** 2026-04-21  
**상태:** 승인됨

---

## 개요

현재 구현된 단순 JWT 로그인/회원가입 구조에 다음을 추가한다:

1. Refresh Token (Token Rotation + Family 기반 탈취 감지)
2. 로그아웃 (jti 기반 액세스 토큰 블랙리스트 + Redis)
3. 이메일 인증 (회원가입 후 UUID 토큰 이메일 발송)
4. 패스워드 강도 검증 (커스텀 어노테이션)

---

## 기술 스택 추가

```groovy
// build.gradle
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
implementation 'org.springframework.boot:spring-boot-starter-mail'
```

```yaml
# application.yml 추가
spring:
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
  mail:
    host: ${MAIL_HOST}
    port: ${MAIL_PORT:587}
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true
```

---

## 데이터 모델

### User 엔티티 수정

기존 `User`에 다음 필드 추가:

```java
private boolean emailVerified = false;
private String emailVerificationToken;       // UUID, nullable
private LocalDateTime emailVerificationTokenExpiresAt; // nullable
```

### 새 엔티티: RefreshToken

```java
@Entity
@Table(name = "REFRESH_TOKEN")
public class RefreshToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;           // UUID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String familyId;        // UUID, 탈취 감지용 계열 식별자

    @Column(nullable = false)
    private LocalDateTime expiresAt; // 발급 시점 + 30일

    private boolean used = false;   // rotation 추적

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
```

### Redis 키 구조

```
blacklist:{jti}  →  "revoked"  (TTL = 액세스 토큰 남은 만료 시간(초))
```

---

## API 엔드포인트

| Method | Path | 인증 | 설명 |
|--------|------|------|------|
| `POST` | `/register` | 없음 | 회원가입, 이메일 인증 발송 |
| `POST` | `/login` | 없음 | 로그인, accessToken + refreshToken 반환 |
| `POST` | `/token/refresh` | 없음 | 리프레시 토큰으로 새 accessToken 발급 |
| `POST` | `/logout` | Bearer | 토큰 무효화 (블랙리스트 + DB 삭제) |
| `GET` | `/verify-email` | 없음 | 이메일 인증 (`?token=UUID`) |

---

## 주요 플로우

### 로그인

```
POST /login
  → AuthenticationManager로 인증
  → accessToken 생성 (jti=UUID 클레임 포함, 만료 10분)
  → refreshToken 생성 (UUID, familyId=UUID 신규 생성, 만료 30일)
  → RefreshToken DB 저장
  → { accessToken, refreshToken } 반환
```

### 토큰 갱신

```
POST /token/refresh  { refreshToken }
  → DB에서 token 조회
  → 없으면 → 401
  → used=true이면 → familyId 전체 RefreshToken 삭제 (탈취 감지) → 401
  → expiresAt 초과이면 → 401
  → 정상이면:
      기존 RefreshToken used=true 처리
      새 RefreshToken 생성 (같은 familyId, 새 UUID token)
      새 accessToken 생성 (새 jti)
  → { accessToken, refreshToken } 반환
```

### 로그아웃

```
POST /logout  (Authorization: Bearer <accessToken>)
  → accessToken에서 jti, 만료시간 추출
  → Redis: SET blacklist:{jti} "revoked" EX {남은초}
  → RefreshToken DB에서 해당 user의 토큰 삭제
  → 200 OK
```

### 회원가입

```
POST /register  { name, email, username, password }
  → 패스워드 강도 검증 (@ValidPassword)
  → 이메일/유저네임 중복 확인
  → User 저장 (emailVerified=false, BCrypt 해시)
  → UUID 인증 토큰 생성 (만료 24시간)
  → User.emailVerificationToken, expiresAt 저장
  → 인증 이메일 발송
  → 200 OK
```

### 이메일 인증

```
GET /verify-email?token={UUID}
  → User에서 emailVerificationToken 조회
  → 없거나 만료이면 → 400
  → User.emailVerified=true
  → User.emailVerificationToken=null, expiresAt=null
  → 200 OK
```

### JWT 필터 (변경사항)

```
요청 수신
  → Authorization: Bearer 헤더 추출
  → JWT에서 jti 추출
  → Redis blacklist:{jti} 존재 여부 확인
  → 블랙리스트에 있으면 → 401
  → 기존 서명/만료 검증
  → emailVerified 확인 (로그인 시 검증)
  → SecurityContext 설정
```

---

## 패스워드 강도 검증

커스텀 어노테이션 `@ValidPassword` 구현:

- 최소 8자
- 대문자 1개 이상
- 소문자 1개 이상
- 숫자 1개 이상
- 특수문자 1개 이상 (`!@#$%^&*` 등)

`RegistrationRequest.password`에 적용.

---

## 에러 처리

| 상황 | HTTP | 메시지 키 |
|------|------|-----------|
| 리프레시 토큰 없음/만료 | 401 | `refresh.token.invalid` |
| 리프레시 토큰 재사용 (탈취) | 401 | `refresh.token.reused` |
| 액세스 토큰 블랙리스트 | 401 | `token.revoked` |
| 이메일 미인증 로그인 시도 | 403 | `email.not.verified` |
| 이메일 인증 토큰 만료/없음 | 400 | `verification.token.invalid` |
| 패스워드 강도 미달 | 400 | `password.strength.invalid` |

---

## 변경 파일 요약

### 신규 생성
- `model/RefreshToken.java`
- `repository/RefreshTokenRepository.java`
- `security/jwt/TokenBlacklistService.java` (Redis)
- `security/jwt/RefreshTokenService.java`
- `security/service/EmailVerificationService.java`
- `service/EmailService.java`
- `configuration/RedisConfiguration.java`
- `security/validation/ValidPassword.java` (어노테이션)
- `security/validation/PasswordConstraintValidator.java`
- `controller/TokenController.java` (refresh, logout)
- `exceptions/TokenControllerAdvice.java`

### 수정
- `model/User.java` — 이메일 인증 필드 추가
- `security/jwt/JwtTokenManager.java` — jti 클레임 추가
- `security/jwt/JwtAuthenticationFilter.java` — 블랙리스트 확인 추가
- `security/jwt/JwtTokenService.java` — 로그인 시 refresh token 생성
- `security/dto/LoginResponse.java` — refreshToken 필드 추가
- `security/dto/RegistrationRequest.java` — @ValidPassword 적용
- `security/service/UserDetailsServiceImpl.java` — emailVerified 확인
- `configuration/SecurityConfiguration.java` — 새 엔드포인트 permitAll 추가
- `build.gradle` — Redis, Mail 의존성 추가
- `application.yml` — Redis, Mail 설정 추가

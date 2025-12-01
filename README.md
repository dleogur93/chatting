# Real-Time Chat Server (Spring Boot · STOMP · Redis Pub/Sub · JWT)

Spring Boot 기반의 실시간 채팅 서버입니다.  
STOMP(WebSocket)와 Redis Pub/Sub 구조를 사용해 안정적인 메시지 전송과 확장성을 고려해 개발했습니다.  
JWT로 인증을 처리하며, 단체 채팅방과 1:1 채팅방 모두 지원합니다.

---

## 기술 스택

**Backend**
- Java 17  
- Spring Boot 3.x  
- Spring WebSocket(STOMP)  
- Spring Security(JWT)  
- Spring Data JPA  

**Infrastructure**
- MySQL  
- Redis Pub/Sub  
- SockJS / STOMP Client  

---

## 주요 기능

### 회원
- 회원가입
- 로그인(JWT 발급)
- 회원 목록 조회

### 채팅
- 그룹 채팅방 생성 / 참여 / 나가기  
- 그룹 채팅방 목록 조회  
- 1:1 채팅방 자동 생성(기존 존재 시 재사용)  
- 메시지 실시간 전송  
- Redis Pub/Sub 기반 메시지 브로드캐스트  
- 메시지 저장  
- 사용자별 읽음 처리(ReadStatus)  
- 내 채팅방 목록 + 읽지 않은 메시지 개수 조회  

---

## 아키텍처 개요

### 전체 흐름
1. 클라이언트는 `/connect` 엔드포인트로 WebSocket 연결 시도  
   → Authorization 헤더에 JWT 포함  
2. STOMP CONNECT 단계에서 JWT 검증  
3. SUBSCRIBE 요청 시 해당 방 참여자인지 추가 검증  
4. 메시지 발행(`/publish/{roomId}`)  
5. 서버는 메시지를 저장한 후 Redis Pub/Sub 채널에 메시지를 publish  
6. 모든 서버 인스턴스가 메시지를 수신해 `/topic/{roomId}` 구독자에게 브로드캐스트  

### 인증 구조
- HTTP 요청은 `JwtAuthFilter`에서 JWT 검증  
- WebSocket 연결(CONNECT)과 SUBSCRIBE도 Interceptor에서 검증  
- 세션은 사용하지 않고(stateless), JWT만으로 인증 처리  

---

## 데이터 모델

### ChatRoom
- 그룹(Y) / 1:1(N) 구분
- 참여자 목록, 메시지 목록

### ChatParticipant
- 특정 방에 속한 사용자 정보

### ChatMessage
- 메시지 내용, 보낸 사람, 채팅방 정보

### ReadStatus
- 사용자가 특정 메시지를 읽었는지 여부 저장

### Member
- 이메일 기반 사용자 정보
- 비밀번호 암호화 저장

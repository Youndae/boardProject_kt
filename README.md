# 환경

---

* Spring Boot 3.2.1
* kapt 1.9.21
* Spring-Data-JPA
* Hibernate
* QueryDSL
* OAuth2
* Slf4j
* MySQL
* Redis
* JWT
* Spring Security

# 목표

---

Clean Architecture 구조로 구현   
기존 BoardProject_REST를 Kotlin으로 구현   
Kotlin class, Data Class 등 객체 생성 및 처리 적응   
Java와의 차이점 확인   


# History

---

## 24/08/06
> 프로젝트 생성   
> gradle 세팅   
> 빌드까지 확인.   
> 문제점. build.gradle.kts 모든라인 빨간줄로 오류 발생한 문제.   
> 문제 해결 -> 블로그에 작성 https://myyoun.tistory.com/239

## 24/09/04
> 프로젝트 시작
> AOP를 제외한 config 파일들 생성 및 작성   
> OAuth2, JWT, Spring Security, CORS, QueryDSL, Redis, Properties 관련 설정 파일 생성 및 작성   
> get 요청을 통한 DB 접근 여부 및 데이터 반환 테스트.

## 24/09/05
> Logger를 util.LogginUtil.kt를 생성해 간단하게 사용할 수 있도록 처리   
> 패키지 구조 확립.   
> 처리 구조 설계.   
>> Controller -> UseCase -> Service(class) -> Repository 형태로 처리.   
>> ResponseEntity 매핑은 이전과 동일하게 ResponseFactory 클래스 생성해서 처리.   
>> UseCase, Service는 Read, Write로 분리.   
>> Service는 인터페이스 없이 클래스로만 생성.   
>> DTO는 Data Class를 최대한 활용하고 상속이 필요한 케이스만 class로 처리하는 방향으로 계획.   
>
> CustomException 클래스들과 ExceptionEntity 생성하고 핸들링 처리.   
> 처리과정에 필요한 Enum 파일들 생성   
> Member 관련 기능 구현 완료.   

## 24/09/06
> Comment 관련 기능 구현 완료.


# 메모 및 느낀점

---

## 24/09/04
> @Value를 바로 사용하기가 어렵다.   
> 다른 방법이 있는지는 모르겠지만 일단 생성자 주입은 안된다는 것 같다.   
> lateinit으로 처리하면 사용할 수 있는 것 같다.

## 24/09/05
> 생각보다 자바랑 차이가 별로 없다.   
> 일단 Spring을 같이 쓴다는 점에서부터 생각보다 엄청 수월하게 처리하는 중.   

## 24/09/06
> Stream 사용에 있어서 아주 약간의 차이가 발생하는데 아직은 많이 어색.   
> Comment 처리를 하면서 RequestDTO에 대해 상속으로 처리하는데 상속받은 필드들도 명시해야 한다는 점이 좀 어색하고 아직은 잘 모르겠다.   
> 자바에서는 상속받아서 굳이 명시 안하고 편하게 사용할 수 있었는데 코틀린은 명시해야 한다고 해서 필드만 상속받는 경우 이게 의미가 있을지 모르겠다.   
> 그래도 많은 차이가 발생하진 않아서 빠르게 마무리할 수 있을듯?
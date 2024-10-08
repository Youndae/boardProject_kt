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

## 24/09/07
> HierarchicalBoard, ImageBoard 기능 구현 완료.
> 모든 기능 구현 완료. 테스트 필요.

## 24/09/08
> 테스트 완료. 기능 정상 수행 확인.   
> 로그 처리 개선.    
>> Java, Spring에서와 다르게 별의 별 로그가 다 찍혔다.   
>> yml에서 logging: level: root: 를 아예 설정하지 않는다면 오류 로그도 찍히지 않는 문제가 있었다.   
>> 문제 해결 방법으로 logback-spring.xml에 ConsoleAppender를 추가하니 문제가 해결.   
>> 처리하면서 jdbc 관련 로그들도 필요한것만 콘솔에 찍고 나머지는 파일로 저장하도록 수정.


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

## 24/09/08
> 코틀린은 로그 설정이 Java, Spring Boot 환경보다 좀 더 복잡한 것 같다.   
> Java, Spring 환경에서는 따로 콘솔에 출력하도록 처리하지 않아도 콘솔에 정상적으로 출력되었는데 여기서는 그게 안된다.   
> 그래도 덕분에 로그 제어에 대해 좀 더 알 수 있었다.   
> 
> 프로젝트를 마무리하면서 느낀점으로는 Null에 대한 생각이 좀 많아졌다.   
> 코틀린이 NullSafety는 NPE를 발생시키지 않는다는 점에서 굉장히 좋다고 생각했었으나 너무 가벼운 생각이었다.   
> 프로젝트를 진행하면서 보통 비즈니스 로직상에서는 ' ? '를 잘 활용해 필요한 부분에 대해 Null 체크를 하도록 처리했다.   
> 하지만 막상 컨트롤러에서 Principal 객체를 받을때는 모두 그냥 받았던 것.   
> 심지어 로그를 보고도 왜? 이렇게 생각할만큼 너무 가볍게 여겼다 ㅠ   
> 권한이 필요하지 않은 요청이지만 principal을 받는 경우가 있다.   
> 게시글 상세 페이지 같은 기능.. 필요한 이유는 사용자의 아이디 또는 닉네임을 응답에 포함시켜 해당 게시글의 수정, 삭제 기능을 처리할 수 있도록 해야 하기 때문.   
> 권한이 필요한 요청에 대해서는 @PreAuthorize를 통해 처리했으니 princpal이 null이 될 수 없다는 보장이 생기지만 그렇지 않은 경우는 null을 허용해야 했다.   
> 그렇기에 해당 부분과 같이 처리되는 컨트롤러에서 오류가 발생한 것.   
> 매번 그 처리를 principal == null 이라는 조건문을 통해 처리해놓고서는 왜 생각도 못하고 놓쳤는지..    
> Null safety에 대해 내가 너무 가볍게 생각했구나, Java에서도 null에 대해 너무 가볍게 바라보고 있었구나라는 걸 뼈저리게 느낄 수 있었다.   
> 
> 복잡한 기능이 없이 CRUD위주의 간단한 처리가 주를 이루는 프로젝트인만큼 코틀린으로 재구현하는데서는 어려움이 없었다.   
> 코틀린의 모든걸 사용해본것은 아니지만 class, data class 를 어떻게 설계하고 사용해야 할지, 가장 기본적인 class, interface의 생성이나 사용, 변수에 대한 처리 등은 찾아가면서 사용할 수 있을 정도는 된 것 같다.   
> 문법 공부할때는 왜 코틀린에서는 Builder 패턴이 비효율적인가에 대해 이해가 되지 않았지만 프로젝트를 하나 진행해보니 이해가 된다. 이런면은 확실히 코틀린의 장점이라고 생각한다.    
> 삼항연산자가 없다는 점은 처음에는 어색했지만 오히려 이게 더 좋은것 아닌가 싶기도 하다. 조건문의 결과를 그대로 변수에 담거나 반환할 수 있다는 점에서 불필요한 코드가 많이 사라졌다.   
> 
> 마지막으로 UseCase를 사용하면서 SOA보다 좀 더 깔끔하게 코드를 작성할 수 있었다고 생각한다.   
> SOA 방식은 아무래도 서비스 메소드에서 다른 서비스를 호출한다거나 컨트롤러가 여러 서비스를 호출하면서 결과에 대한 매핑을 mapper, converter를 통해 처리한다거나 하는 방식이었기 때문에 컨트롤러에 책임이 과하진 않나? 라는 고민을 많이 했었다.   
> 하지만 UseCase가 캡슐화를 담당하게 되면서 컨트롤러는 간결하게 UseCase 호출 및 응답 매핑, 반환만을 담당하게 분리할 수 있었고, UseCase에서 각 비즈니스 로직을 호출하고 처리하는 책임을 갖게 되었다.   
> 이렇게 처리하니 서비스 메소드들도 좀 더 명확하게 분리할 수 있었고 다른 서비스를 호출하는 경우를 최소화할 수 있었다.   
> 어느 하나만 고집할 수는 없겠지만 비즈니스 로직에 중점을 두고 생각하면 UseCase를 사용하는 편이 추후 관리와 기능 추가등을 고려했을 때 설계가 수월하지 않을까 라는 생각이 들었다.
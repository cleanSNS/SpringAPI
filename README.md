# ✏️ Clean SNS

인공지능을 활용하여 글을 분석하고 욕설을 판별하여 해당부분만을 검열하는 SNS입니다

**사이트 링크** : [https://cleanbook.site]

<br/>

<br/>

**[ 백엔드(Spring) ]** : 임채민

**[ 프론트(React) ]** : 성의현

**[ CI/CD 환경 구축(Jenkins) ]** : 임채민

**[ 기획, 디자인 ]** : 임채민, 성의현, 이종찬

<br/>

### 📝 프로젝트때 작성한 문서 & 진행도

---

- [프로젝트 노션 ](https://alive-pocket-572.notion.site/c563651ed195442990dc9bf82388d13b)
- [API Docs](https://alive-pocket-572.notion.site/API-docs-e3d1ed1dd409449e95a791147423c39e)

<br/>

### 🏗️ 기능

---

- 원하는 분야의 스터디 모집하거나 모집중인 스터디를 탐색할 수 있습니다.
- 원하는 스터디에 신청할 때, 간단한 자기소개 글을 작성하면 회원 정보와 혼합된 이력서 형식으로 제출됩니다.
- 불쾌한 내용이 담긴 스터디를 신고해 일정횟수 이상 스터디 신고가 누적되었을 경우 경고 표시를 띄웁니다.
- 마음에 드는 스터디를 즐겨찾기해 즐겨찾기 한 스터디로 관리할 수 있습니다.
- 생성한 스터디, 신청한 스터디, 즐겨찾기한 스터디별로 분리해 관리할 수 있습니다.
- 스터디에 참여중인 회원을 확인하고 스터디장은 스터디에 신청한 회원들의 참여여부를 수정할 수 있습니다.

<br/>



### 😎 Spring 백엔드 서버를 만들며 경험한 것

---

- 쿠키를 이용한 상태정보 유지
- MySQL version에 따른 보안 정책
- Docker를 이용한 port forwarding 경험
- CORS 설정
- AWS를 활용한 TCP/IP 통신 경험

<br/>  

### 🛠️ Spring을 사용한 백엔드 구축 중 어려웠던 것

------

<h4>1. mysql과 spring boot 연결오류 </h4>

프로젝트가 어느정도 진행된 후 테스트하는 단계에서 로컬에선 잘 연결되던 것이 jar파일로 만든 후 실 운영 서버에서 테스트 할 때는 연결이 되지 않는 오류가 발생했습니다.

```cpp
com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure
```

다음과 같은 에러였는데 당시 구글링 한 결과 제시된 해결책은 다음과 같았습니다.

- autoreconnect 설정
- 접속 url을 제대로 작성(port번호, id, pw)
- RDS 보안그룹 설정
- jdbc와 mysql간 버젼 호환성

그러나 위 4가지 방법은 저희의 경우엔 적용되지 않았고, 찾아낸 원인은 바로 TLS version 차이였습니다.

> https://dev.mysql.com/doc/refman/5.7/en/encrypted-connections.html mysql 5.7.35부터는 tls v1.0, 1.1을 사용하지 않고 오직 tls v1.2로만 통신하도록 지원합니다.

당시 저희가 사용하던 mysql은 8.0.24 버젼이었지만 MySQL Server에서 허용하는 tls는 v1.0, 1.1, 1.2, 1.3이었습니다. 이때 spring에서 자동으로 1.0혹은 1.1 version의 tls protocol로 통신하도록 설정되어서 연결 오류가 발생했다고 판단하였고(앞에 있을 수록 우선순위가 높을 거라 예상합니다.), 접속 url에 다음과 같이 사용할 TLSProtocol을 지정해주었더니 문제가 해결되었습니다.

```cpp
?enabledTLSProtocols=TLSv1.2
```



<h4> 2. CORS </h4>

이번 프로젝트에서 Spring 백엔드 팀이 가장 애먹었던 부분은 CORS였습니다.

프론트엔드의 React 코드가 백엔드의 Spring 리소스를 요청하는 과정에서 CORS 에러가 발생했는데 CORS에 대한 이해가 부족했던터라 개념을 이해하고 코드에 적용시키는 과정에 많은 시행착오가 있었습니다.

**교차 출처 리소스 공유(Cross-Origin Resource Sharing, CORS)**는 추가 HTTP 헤더를 사용하여, 한 출처에서 실행 중인 웹 애플리케이션이 다른 출처의 선택한 자원에 접근할 수 있는 권한을 부여하도록 브라우저에 알려주는 체제입니다. 웹 애플리케이션은 리소스가 자신의 출처(도메인, 프로토콜, 포트)와 다를 때 교차 출처 HTTP 요청을 실행합니다.

**Access-Control-Allow-Origin** 헤더에 허용하고 싶은 출처를 세팅하고 **Access-Control-Allow-Methods** 헤더에 허용하고 싶은 HTTP method를 지정함으로써 기본적인 CORS 에러를 해결할 수 있었습니다. 

하지만 쿠키를 통해 사용자인증이 필요한 API에서는 여전히 CORS 에러가 발생했고 출처가 다른 http 통신에서는 request header에 쿠키가 자동으로 들어가지 않는것이 원인이었습니다. 쿠키를 요청에 포함시키기 위해서 헤더에 **Access-Control-Allow-Credentials : true**를 추가함으로써 문제를 해결했습니다.

아래의 코드와 같이 앞서 서술한 내용들을 담은 WebConfig 클래스를 빈으로 등록해 CORS 설정을 마쳤습니다.

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000","https://catchstudys.com"
                                ,"https://www.catchstudys.com")
                .allowedMethods("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

<br/>




### **🛠 보완해야 할 점**

---

### 1. **DB설계의 중요성**

초기에 설계한 DB 스키마를 사용하다가 일부 테이블에서 정규화 원칙을 제대로 지키지 않고 설계한 점이 추후에 드러났습니다. 이를 통해 프로젝트 후반에 스키마를 수정하거나 기능 확장에 어려움이 생기는 등 여러 문제점이 발생했습니다.

예를 들어 기존에는 study에 category라는 칼럼이 없었고 전체 검색과정에서 study와 연관된 기술 목록에서 category를 읽어오는 식으로 구현했는데, 연관된 기술이 없는 경우나 여러 category가 여러 개 생기는 등의 문제점이 발생했습니다. 처음부터 기술목록들을 분류하는 category라는 스키마를 만든 후 이를 스터디와 연관시켜주었다면 카테고리에서 벗어난 기술들을 스터디에 추가하지 못하게 하는 등의 제한이 가능했을텐데 이 점이 아쉬웠습니다.

### **2. 기획에서 벗어난 역할 분배**

처음 역할을 분배할 때 만들어질 것이 예상되는 페이지 별로 역할을 나누었습니다. 예를 들어 나의 스터디 관리 페이지에서 사용할 기능들은 A라는 팀원이 맡고 메인 페이지에서 사용할 기능들은 B라는 팀원이 맡는 식이었습니다. 그리고 개발단계에서 이런 역할분배가 비효율적임을 느끼게 되었습니다.

상기한 역할분배가 비효율적임을 느낀 이유는 바로 같은 테이블에 관련된 기능을 구현하는데 누구는 조회를 구현하고 누구는 생성,수정 등을 구현하니까 자료형, 메소드 등을 수정하는 데 큰 번거로움이 있다는 것이었습니다. 테이블에서 정보를 불러오는 메소드를 A와 B가 같이 사용하는데 A가 해당 메소드를 수정하면 B가 구현한 기능이 제대로 동작하지 않는 등의 오류가 발생하거나 B가 테이블에서 정보를 불러올 때 사용하는 Dto를 변경하면 A가 그에 맞추어서 기능을 수정하다가 오류가 발생하는 등 여러 어려움이 있었습니다.

따라서 다음에 역할분배를 할 때는 테이블별로 역할을 나누어 서로의 코드가 충돌하는 일이 없도록 기획하기로 다짐하게 되었습니다.

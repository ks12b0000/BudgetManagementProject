![logo](https://github.com/ks12b0000/wanted-pre-onboarding-budget-management/assets/102012155/f0473883-cac0-4362-8273-dc9462a7ba8e) 

# 예산 관리 어플리케이션 

<br/>

## 프로젝트 기간 
2023-11-09 ~ 2023-11-17

<br/>

## Table of Contents
- [개요](#개요)
- [Skils](skils)
- [ERD](#erd)
- [프로젝트 설계 및 일정관리](#프로젝트-설계-및-일정관리)
- [API Reference](#api-reference)
- [API 구현과정 및 고려사항](#api-구현과정-및-고려사항)
- [Test](#test)


<br/>

## 개요 
본 서비스는 사용자들이 개인 재무를 관리하고 지출을 추적하는 데 도움을 주는 애플리케이션입니다. 이 앱은 사용자들이 예산을 설정하고 지출을 모니터링하며 재무 목표를 달성하는 데 도움이 됩니다.

<br/>

## Skils
언어 및 프레임워크: ![Static Badge](https://img.shields.io/badge/Java-red) 
![Static Badge](https://img.shields.io/badge/SpringBoot-grean)
![Static Badge](https://img.shields.io/badge/SpringDataJPA-grean)
<br/>
데이터 베이스: ![Static Badge](https://img.shields.io/badge/mysql-blue)

<br/>

## ERD
<img width="470" alt="스크린샷 2023-11-10 오후 3 44 26" src="https://github.com/ks12b0000/wanted-pre-onboarding-budget-management/assets/102012155/ca9b4aa3-771d-466c-8924-7823cd9e1a04">

<br/>

## 프로젝트 설계 및 일정관리
[![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)](https://www.notion.so/dev-j/6a83f5bfa7874dc49e4fac30653aaa53?v=25c6ca9163064a8c879dcf124a914f29&pvs=4)
<br/>
[프로젝트 설계 및 일정관리 페이지](https://purple-knot-a8d.notion.site/a0f3cdc8e24b45f2a00d7105df36426f?pvs=4)

<img width="600" alt="스크린샷 2023-11-15 오후 4 01 40" src="https://github.com/ks12b0000/BudgetManagementProject/assets/102012155/26852659-dfd6-4185-8f43-2bda4a9df9ff">
<img width="600" alt="스크린샷 2023-11-15 오후 4 03 53" src="https://github.com/ks12b0000/BudgetManagementProject/assets/102012155/338212df-e77a-4c24-a24c-223fa6c24385">

<br/>

## API Reference
<img src="https://img.shields.io/badge/Swagger-6DB33F?style=for-the-badge&logo=SWAGGER&logoColor=white">
<img width="701" alt="스크린샷 2023-11-15 오후 3 27 55" src="https://github.com/ks12b0000/BudgetManagementProject/assets/102012155/dd03410d-fbce-406a-9559-2de46768c050">
<br/>

## API 구현과정 및 고려사항
<details>
<summary>Budgets - click</summary>

  - Budget 카테고리 목록 조회
      - [GET] /api/budget-categories
      - API요청이 오면 등록되어 있는 예산 카테고리 목록을 반환했습니다.
        
  - Budget 설정
      - [POST] /api/budgets
      - RequestBody로 예산 카테고리, 기간, 설정 예산 금액으로 예산을 설정합니다.
        - ex) 2023-11월 식비 : 40만원, 2023-11월 교통 : 20만원
      - 만약 빈 값이 들어오거나, 예산을 설정하려는 카테고리가 없고, 이미 설정한 예산이 들어올 경우(같은 월에 같은 카테고리는 하나 이상 설정이 불가능하다. 수정은 수정 API에서 해야한다.) 예외처리를 했습니다.
      - 예산은 년 월만 필요하므로 request에서 YearMonth로 받아 LocalDate로 매핑 후 DB에 저장했습니다.
            
  - Budget 수정
      - [PATCH] /api/budgets/{budgetId}
      - RequestBody로 설정 예산을 받고 Parameter로 budgetId를 받고 header로 넘어온 토큰의 유저로 예산을 수정합니다.
      - 만약 빈 값이 들어오거나, 예산을 수정하려는 예산이 없고, 수정할 예산의 유저와 header로 받은 유저와 다를 경우 예외처리를 했습니다.
      - 예산은 기간, 카테고리는 수정이 불가능하고 예산 금액만 수정이 가능하도록 했습니다.
        
  - Budget 설계 추천
      - [GET] /api/budgets/recommend
      - Parameter로 총액(ex. 100만원)을 받아 카테고리별 예산을 자동으로 추천해줍니다.
      - 자동 생성된 예산은, 기존 이용중인 유저들이 설정한 평균값으로 지정하였습니다.
        - ex) 기존 유저들이 설정한 카테고리별 예산을 통계하여, 평균적으로 40%를 식비에 30%를 주거에 설정했다면 이 %에 맞게 Parameter로 받은 총액을 나눠 추천하였습니다.
      - 총액을 나눈 카테고리별 예산 금액을 front에서 받아 필요에 따라 유저들은 값들을 변경할 수 있습니다. 이는 수정API에서 하는 것이 아닌.(현재 API는 추천 금액만 받고, 저장은 아직 안했기 때문.) 저장 버튼을 누를 시 front에서 예산 설정API에 값들을 넘겨 예산을 설정하도록 했습니다.

</details>

<details>
<summary>Expenditures - click</summary>

  - Expenditure 생성
      - [POST] /api/expenditures
      - RequestBody로 지출 날짜, 지출 금액, 카테고리, 메모로 지출을 생성합니다 지출이 생성되면 유저, 카테고리, 날짜로 예산을 찾아 유저의 예산에서 마이너스 해줍니다.
      - 만약 메모를 제외한 나머지 값들이 빈 값이 들어오거나, 존재하지 않은 카테고리, 예산 설정을 안한 월과 카테고리일 경우 예외처리를 했습니다.
  - Expenditure 수정
      - [PATCH] /api/expenditures/{expenditureId}
      - RequestBody로 지출 날짜, 지출 금액, 카테고리, 메모로 지출을 수정합니다.
      - 만약 메모를 제외한 나머지 값들이 빈 값이 들어오거나, 존재하지 않은 카테고리, 존재하지 않은 지출 아이디, 수정할 지출의 유저와 header로 받은 유저와 다를경우 예외처리를 했습니다.
  - Expenditure 목록 조회
      - [GET] /api/expenditures
      - RequestBody로 기간, 카테고리, 최소, 최대 금액으로 지출 목록을 조회합니다.
      - 조회된 모든 내용의 지출 합계와 카테고리별 지출 합계를 같이 반환합니다.
      - 만약 존재하지 않은 카테고리가 들어온다면 예외처리를 했습니다.
  - Expenditure 상세 조회
      - [GET] /api/expenditures/{expenditureId}
      - expenditureId로 지출 내역을 찾아 메모, 기간, 카테고리, 금액을 반환했습니다.
      - 만약 존재하지 않은 지출 아이디, 조회할 지출의 유저와 header로 받은 유저와 다를경우 예외처리를 했습니다. 
  - Expenditure 삭제
      - [DELETE] /api/expenditures/{expenditureId}
      - expenditureId로 지출 내역을 찾아 삭제를 합니다.
      - 만약 존재하지 않은 지출 아이디, 삭제할 지출의 유저와 header로 받은 유저와 다를경우 예외처리를 했습니다. 
  - Expenditure 합계 제외
      - [PATCH] /api/expenditures/except/{expenditureId}
      - 지출 목록을 조회할 때 목록에는 포함되지만 합계 금액에는 포함되거나, 되지 않게 설정하고 싶을 경우를 생각해 만들었습니다.
      - Parameter로 받은 excludingTotal로 지출 합계 제외 여부를 변경합니다. false = 합계 제외 X true = 합계 제외 O
      - 만약 존재하지 않은 지출 아이디, 업데이트할 지출의 유저와 header로 받은 유저와 다를경우 예외처리를 했습니다. 
      
  - Expenditure 추천
      - [GET] /api/expenditure/recommend
      - 오늘 날짜, 이번 달 마지막 날짜의 차이를 구해 오늘 날짜에 지출 가능한 금액을 총액과 카테고리별 금액으로 제공하도록 했습니다.
          - ex) 11월 9일 지출 가능 금액 총 30,000원, 식비 15,000 … 으로 페이지에 노출 예정.
      - 고려사항 1. 앞선 일자에서 과다 소비하였다 해서 오늘 예산을 극히 줄이는것이 아니라, 이후 일자에 부담을 분배한다.
          - 오늘 날짜, 이번 달 마지막 날짜의 차이로 남은 기간동안의 예산을 구해 나누게 설계하여 위 고려사항을 해결했습니다.
      - 고려사항 2. 기간 전체 예산을 초과 하더라도 0원 또는 음수 의 예산을 추천받지 않아야 한다.
          - 기간 전체 예산을 초과하게 되면 적절 최소 금액을 20,000원으로 설정하여 추천하도록 진행하였습니다. 
      - 고려사항 3. 유저의 상황에 맞는 문장의 멘트 노출을 한다.
          - 만약 전체 카테고리에서 하나의 카테고리라도 설정한 예산을 초과했을 경우 ex) "(예산 초과한 카테고리), (예산 초과한 카테고리)에 예산을 초과하셨네요! 오늘은 최소 20,000원 이하의 금액만 사용하시는걸 권장해 드리고 앞으로 남은 날은 조금 아껴 쓰셔야 하겠어요!" 멘트를 노출하도록 설정하였습니다.
          - 만약 전체 카테고리에서 예산을 초과한 카테고리가 없을 경우. ex) "절약을 잘 실천하고 계시네요! 앞으로 남은 날도 절약을 위해 화이팅!" 멘트를 노출하도록 설정하였습니다.
      - 고려사항 4. 15333원 과 같은 값이라면 백원 단위 반올림 등으로 사용자 친화적이게 변환한다.
          - 오늘 날짜에 지출 가능한 금액을 가져올 때 JPA 쿼리를 직접 작성해 DB에서 round를 사용해 반올림 하여 가져오도록 설정하였습니다.
      - 매일 08:00 시 유저에게 알림을 발송해 오늘의 지출 추천 내용을 보내줍니다.
          - 모든 유저한테 알림을 발송할 수 있게 위 로직을 가져와 사용할 수 있는 스케줄러 코드를 만들어 모든 유저의 맞는 상황에 따라 지출 추천 내용을 발송 해줄 수 있도록 설정하였습니다.
          - ex) <img width="500" alt="스크린샷 2023-11-17 오후 6 26 11" src="https://github.com/ks12b0000/BudgetManagementProject/assets/102012155/ede16028-6ea8-4f39-9268-6b5a959cc88e">

  - Expenditure 안내
      - [GET] /api/expenditure/guide
      - 고려사항: 일자기준 오늘 지출 금액, 오늘 기준 사용한 금액,위험도를 구해 반환해야된다.
      - 오늘 날짜로 오늘 사용한 카테고리별 지출 금액, 총 지출 금액을 구하고, 오늘 날짜, 이번 달 마지막 날짜로 이번 달 남은 날의 기간을 구해 오늘 사용했으면 적절한 금액을 구해 반환하도록 설계하였습니다. 만약 예산이 초과된 카테고리가 있다면 최소 금액을 20,000원으로 설정.
      - 매일 20:00시에 모든 유저에게 알림을 발송해 오늘의 지출 내역을 안내해줍니다.
        - 모든 유저한테 알림을 발송할 수 있게 위 로직을 가져와 사용하는 스케줄러 코드를 만들어 모든 유저의 맞는 지출 내역 안내 내용을 발송해줄 수 있도록 설정하였습니다.
        - ex) <img width="500" alt="스크린샷 2023-11-17 오후 6 31 54" src="https://github.com/ks12b0000/BudgetManagementProject/assets/102012155/03fce9c3-301e-4ccb-908b-b43b1176c18c">

  - Expenditure 통계(완료 후 작성 아직 진행 중)
      - [GET] /api/expenditure/statistics
            
</details>

<details>
<summary>Users - click</summary>
  
  - User 회원가입
      - [POST] /api/users
      - RequestBody로 이메일과 패스워드를 받아 패스워드 암호화 후 회원가입을 한다.
      - 만약 빈 값이 들어오거나, 중복된 이메일이라면 예외처리를 한다.
   
  - User 로그인
      - [POST] /api/users/login
      - RequestBody로 이메일과 패스워드를 받아 로그인을 한다.
      - 만약 빈 값이 들어오거나, 등록된 이메일이 없고, 이메일은 있는데 패스워드가 맞지 않는다면 예외처리를 한다.
      - 로그인에 성공하면 JWT Token이 발급된다.

</details>
<br/>

## Test
- ServiceTest, RepositoryTest, ControllerTest 단위로 총 47개의 테스트 코드 작성
<img width="194" alt="스크린샷 2023-11-15 오후 3 53 33" src="https://github.com/ks12b0000/BudgetManagementProject/assets/102012155/3a034974-a9dd-464f-ab48-62540bd4ab55">


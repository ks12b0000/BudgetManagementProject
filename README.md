![logo](https://github.com/ks12b0000/wanted-pre-onboarding-budget-management/assets/102012155/f0473883-cac0-4362-8273-dc9462a7ba8e) 

# 예산 관리 어플리케이션 
<p>
<img src="https://img.shields.io/github/issues-pr-closed/ks12b0000/wanted-pre-onboarding-budget-management?color=blueviolet"/>
<img src="https://img.shields.io/github/issues/ks12b0000/wanted-pre-onboarding-budget-management?color=inactive"/>
<img src="https://img.shields.io/github/issues-closed/ks12b0000/wanted-pre-onboarding-budget-management"/> 
</p>

<br/>

- [개요](#개요)
- [유저스토리](#유저스토리)
- [도메인 모델링](#도메인-모델링)
- [API 설계](#api-설계)
- [ERD](#erd)
  
<br/>

## 프로젝트 기간 
2023-11-09 ~ 2023-11-14

<br/>

## 개요 
본 서비스는 사용자들이 개인 재무를 관리하고 지출을 추적하는 데 도움을 주는 애플리케이션입니다. 이 앱은 사용자들이 예산을 설정하고 지출을 모니터링하며 재무 목표를 달성하는 데 도움이 됩니다.

<br/>

## 유저스토리
- A. 유저는 본 사이트에 들어와 회원가입을 통해 서비스를 이용합니다.
- B**. 예산 설정 및 설계 서비스**
    - 월별 총 예산을 설정합니다.
    - 본 서비스는 카테고리 별 예산을 설계(=추천)하여 사용자의 과다 지출을 방지합니다.
- C. 지출 기록
    - 사용자는 지출 을 금액, 카테고리 등을 지정하여 등록 합니다. 언제든지 수정 및 삭제 할 수 있습니다.
- D. 지출 컨설팅
    - 월별 또는 주별 설정한 예산을 기준으로 오늘 소비 가능한 지출 을 알려줍니다.
    - 매일 발생한 지출 을 카테고리 별로 안내받습니다.
- E. 지출 통계
    - 지난 달 대비 , 지난 요일 대비, 다른 유저 대비 등 여러 기준 카테고리 별 지출 통계를 확인 할 수 있습니다.

## 도메인 모델링 

<details>
<summary>User - click</summary> 
  
  - id 
  
  - email 
  
  - password 
  
  - refresh_token 
</details>

<details>
<summary>BudgetCategory - click</summary> 
  
  - id
    
  - name
</details>

<details>
<summary>Budget - click</summary>
  
  - id
  
  - userId
  
  - categoryId
  
  - money
  
  - period
</details>

<details>
<summary>Expenditure - click</summary>
  
  - id
  
  - period
  
  - money
  
  - categoryId
  
  - memo
  
  - excludingTotal
  
  - userId
</details>

<br/>

## API 설계 
<details>
<summary>Users - click</summary>
  
  - User 회원가입
      - [POST] /api/users
      - 이메일과 패스워드를 입력하여 회원가입을 한다.
   
  - User 로그인
      - [POST] /api/users/login
      - 이메일과 패스워드를 입력하여 로그인을 한다.
      - 로그인 시 JWT Token이 발급된다.

</details>

<details>
<summary>Budgets - click</summary>

  - Budget 카테고리 목록 조회
      - [GET] /api/budget-categories
      - 등록되어 있는 예산 카테고리 목록을 반환한다.
        
  - Budget 설정
      - [POST] /api/budgets
      - 해당 기간 별 설정한 예산을 설정합니다. 예산은 카테고리를 필수로 지정합니다.
          - ex) 식비 : 40만원, 교통 : 20만원
            
  - Budget 수정
      - [PATCH] /api/budgets
        
  - Budget 설계 추천
      - [GET] /api/budgets/recommend
      - 카테고리 지정 없이 총액 (ex. 100만원) 을 입력하면, 카테고리 별 예산을 자동 생성.
      - 자동 생성된 예산은, 기존 이용중인 유저 들이 설정한 평균 값 입니다.
          - 유저들이 설정한 카테고리 별 예산을 통계하여, 평균적으로 40% 를 식비에, 30%를 주거 에 설정 하였다면 이에 맞게 추천.
          - 10% 이하의 카테고리들은 모두 묶어 기타 로 제공한다.(8% 문화, 7% 레져 라면 15% 기타로 표기)
          - **위 비율에 따라 금액이 입력됩니다.**
              - **ex) 식비 40만원, 주거 30만원, 취미 13만원 등.**
      
      **추가설명**
      
      유저는 예산 설정 페이지 에서 카테고리별로 예산을 설정 합니다. 이를 지정하기 어려운 유저들은 예산 추천 기능을 사용하고 클릭 시, 자동으로 페이지 상 카테고리 별 예산이 입력됩니다. 유저는 입력 된 값들을 필요에 따라 수정(API 가 아닌 화면에서) 한 뒤 이를 저장(=예산설정 API)합니다.

</details>

<details>
<summary>Expenditures - click</summary>

  - Expenditure 생성
      - [POST] /api/expenditures
      - 지출 일시, 지출 금액, 카테고리 와 메모 를 입력하여 생성합니다.
  - Expenditure 수정
      - [PATCH] /api/expenditures/{id}
      - 생성한 유저만 위 권한을 가집니다.
  - Expenditure 목록 조회
      - [GET] /api/expenditures
      - 필수적으로 기간 으로 조회 합니다.
      - 조회된 모든 내용의 지출 합계, 카테고리 별 지출 합계 를 같이 반환합니다.
      - 특정 카테고리만 조회.
      - 최소, 최대 금액으로 조회.
          - ex) 0~10000원 / 20000원 ~ 100000원
  - Expenditure 상세 조회
      - [GET] /api/expenditures/{id}
      - 전체 필드값을 반환한다.
  - Expenditure 삭제
      - [DELETE] /api/expenditures/{id}
  - Expenditure 합계 제외
      - [PATCH] /api/expenditures-except/{id}
      
  - Expenditure 추천
      - [GET] /api/expenditure/recommend
      - 오늘 날짜에 지출 가능한 금액을 총액과 카테고리별 금액으로 제공합니다.
          - ex) 11월 9일 지출 가능 금액 총 30,000원, 식비 15,000 … 으로 페이지에 노출 예정.
      - 고려사항 1. 앞선 일자에서 과다 소비하였다 해서 오늘 예산을 극히 줄이는것이 아니라, 이후 일자에 부담을 분배한다.
          - 앞선 일자에서 사용가능한 금액을 1만원 초과했다 하더라도, 오늘 예산이 1만원 주는것이 아닌 남은 기간 동안 분배해서 부담(10일 남았다면 1천원 씩).
      - 고려사항 2. 기간 전체 예산을 초과 하더라도 0원 또는 음수 의 예산을 추천받지 않아야 한다.
          - 지속적인 소비 습관을 생성하기 위한 서비스이므로 예산을 초과하더라도 적정한 금액을 추천받아야 합니다.
          - 최소 금액을 자유롭게 설정.
      - 유저의 상황에 맞는 문장의 멘트 노출.
          - 잘 아끼고 있을 때, 적당히 사용 중 일 때, 기준을 넘었을때, 예산을 초과하였을 때 등 유저의 상황에 맞는 메세지를 같이 노출합니다.
          - 조건과 기준은 자유롭게 설정.
          - ex) “절약을 잘 실천하고 계세요! 오늘도 절약 도전!” 등
      - 15333원 과 같은 값이라면 백원 단위 반올림 등으로 사용자 친화적이게 변환.
      - 매일 08:00 시 알림 발송
  - Expenditure 안내
      - [GET] /api/expenditure/guide
      - 오늘 지출한 내용을 총액과 카테고리별 금액을 알려줍니다.
      - 월별설정한 예산 기준 카테고리 별 통계 제공
          - 일자기준 오늘 적정 금액 : 오늘 기준 사용했으면 적절했을 금액
          - 일자기준 오늘 지출 금액 : 오늘 기준 사용한 금액
          - 위험도 : 카테고리 별 적정 금액, 지출금액의 차이를 위험도로 나타내며 %(퍼센테이지) 입니다.
              - ex) 오늘 사용하면 적당한 금액 10,000원/ 사용한 금액 20,000원 이면 200%
      - 매일 20:00 시 알림 발송
  - Expenditure 통계
      - [GET] /api/expenditure/statistics
      - 지난 달 대비 총액, 카테고리 별 소비율 반환.
          - 오늘이 10일차 라면, 지난달 10일차 까지의 데이터를 대상으로 비교
          - ex) 식비 지난달 대비 150%
      - 지난 요일 대비 소비율
          - 오늘이 월요일 이라면 지난 월요일 에 소비한 모든 기록 대비 소비율
          - ex) 월요일 평소 대비 80%
      - 다른 유저 대비 소비율
          - 오늘 기준 다른 유저 가 예산 대비 사용한 평균 비율 대비 나의 소비율
          - 오늘기준 다른 유저가 소비한 지출이 평균 50%(ex. 예산 100만원 중 50만원 소비중) 이고 나는 60% 이면 120%.
          - ex) 다른 사용자 대비 120%
            
</details>
<br/>

## ERD
<img width="470" alt="스크린샷 2023-11-10 오후 3 44 26" src="https://github.com/ks12b0000/wanted-pre-onboarding-budget-management/assets/102012155/ca9b4aa3-771d-466c-8924-7823cd9e1a04">

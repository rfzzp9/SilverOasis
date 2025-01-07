![그림01](https://github.com/user-attachments/assets/0ed442c9-ffd0-4ff4-b1dc-3a5e912b6ab9)![그림01](https://github.com/user-attachments/assets/5482c996-21f4-4b05-bf9e-226ca9d45f74)# Silver Oasis



노인 사용자의 특성을 고려하여 앱 사용자들의 노인 복지 시설 선호도, 별점을 기준으로 상위 순위의 노인 복지 시설 정보들을 추천해주는 서비스입니다.


## 📌 주요 기능


- 사용자에게 적합한 노인 복지 시설 정보 제공
- 사용자 커뮤니티 및 스크랩 기능


## 🛠 기술 스택


- FRONT-END PART


|구분|Skill|
|------|---|
|Platform|Android Studio|
|Language|Java|
|Networking|Volley|
|Design|Figma|
|ETC|SharedPreferences|


## 📌 개발 내용


- Volley 라이브러리를 사용하여 서버와의 통신 구현
    - 복지 시설 정보, 댓글, 회원 정보, 커뮤니티 등을 요청 및 수신
- Android XML로 UI 구현
- 웹뷰로 커뮤니티 화면, 회원가입 화면, 댓글 화면 연결


## 📌 성장 경험


### *화면 전환 시 다중 클릭으로 인한 레이아웃 겹침 현상 해결*

버튼 클릭 시 다음 화면으로 전환되는 과정에서 초기 화면에서 버튼을 여러 번 클릭할 경우 중복된 화면 전환이 발생하여 레이아웃이 겹쳐 보이는 문제가 발생했습니다.

클릭 이벤트가 처리되는 동안 사용자가 추가로 클릭할 수 있는 상태였기에 UI 쓰레드에서 기존 작업이 완료되기 전에 새로운 요청이 실행되면서 화면이 중첩되는 현상이 발생한 것입니다.

이를 해결하기 위해 버튼이 클릭된 후 일정 시간 동안 추가 클릭이 발생하지 않도록 버튼을 비활성화하는 방지 로직을 추가했습니다.

이를 통해 중복 화면 전환 요청을 방지하여 레이아웃 겹침 현상을 완전히 제거할 수 있었습니다.


### *모바일 환경에 대한 이해*

사용자 인터렉션 관리에 대해 학습하면서 모바일 환경에 대한 이해도를 높였습니다. 특히 화면 전환에 대해 중점적으로 학습했습니다.


### *비개발 직군과 협업*

팀원 4명이 기획 단계부터 모든 과정을 함께하며, 개발 과정에서도 지속적으로 소통을 이어갔습니다. 기술적으로 해결 가능한 문제와 그렇지 않은 문제를 함께 논의하며, 서비스를 꾸준히 발전시켜 나갔습니다.
기능 구현에 그치지 않고, 서비스의 비즈니스를 깊이 이해하며 서비스에 가치를 담고자 노력했습니다.


## 📌 서비스 화면


(시연 영상 - *https://youtu.be/_hfVHI4AI6Y*)

<div align="center">

| ![image1](https://github.com/user-attachments/assets/0f5eb351-7be6-4ba8-83ab-95a9d4dc34f0) | ![image2](https://github.com/user-attachments/assets/dfd37f24-24e9-4dfb-9329-780e4feabea5) |
|:--------------------------------------------------------:|:--------------------------------------------------------:|
| **스클래시 화면**                                  | **메인 화면**                                  |
| ![image3](https://github.com/user-attachments/assets/975eb921-70ad-478d-b17d-91b3bf5cc933) | ![image4](https://github.com/user-attachments/assets/cc70a6ba-5253-414c-91da-a708049e403a) |
| **노인 복지 시설 추천 결과 화면**                                  | **노인 복지 시설 상세 화면**                                  |
| ![image5](https://github.com/user-attachments/assets/df72eff0-d8e6-43c4-a8c4-d19eeadf3ea6) | ![image6](https://github.com/user-attachments/assets/d21f8106-25cc-422a-ab50-dcdc9a97347f) |
| **스크랩 화면**                                  | **게시판 화면**                                  |

</div>



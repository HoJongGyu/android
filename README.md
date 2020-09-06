앱 설명
-------------------
안드로이드 뮤직 어플 입니다    

자신이 추가 하고 싶은 음악이나 영상에 대한 링크를 추가하여 음악과 유튜브를 감상 할 수 있습니다. 

Retrofit으로 직접 구현한 스프링 API서버를 통해 회원가입과 로그인을 구현 하였고 Room으로 음악에 대한 정보를 관리 합니다

이것을 Room에서 받아와 미디어 플레이어로 재생 시킵니다. 미디어 플레이어는 서비스로 구현하여 백그라운드 재생을 하였습니다.   

추가적으로 브로드 캐스트, 오디오 포커스등을 사용 하였습니다.   

뷰모델에 각 모델에 대한 데이터를 저장 하였고 데이터바인딩으로 UI에 출력해 데이터에 일관성을 주었습니다    

또한, 뷰모델에 데이터를 이용하여 미디어 플레이어 서비스에 대한 기능을 구현 하였고 백그라운드를 구현 하였습니다.   



Screenshots
-----------------

<div>
 <img width="200" src="https://user-images.githubusercontent.com/70811978/92327189-e076e080-f092-11ea-971c-738a87a60212.jpg">
  
<img width="200" src="https://user-images.githubusercontent.com/70811978/92327196-ea004880-f092-11ea-941b-d3922e50ed30.jpg">
  

<img width="200" src="https://user-images.githubusercontent.com/70811978/92327322-bd98fc00-f093-11ea-914c-ed127a0abcba.jpg">
  
<img width="200" src="https://user-images.githubusercontent.com/70811978/92327198-ed93cf80-f092-11ea-8e7c-bd73afd3f7c5.jpg">


<img width="200" src="https://user-images.githubusercontent.com/70811978/92327331-ce497200-f093-11ea-80f3-934041b2fd58.jpg">

<img width="200" src="https://user-images.githubusercontent.com/70811978/92327340-dacdca80-f093-11ea-83c7-4d52118380a3.jpg">

<img width="200" src="https://user-images.githubusercontent.com/70811978/92327341-dbfef780-f093-11ea-9e68-bc1357a9a200.jpg">
  
</div>

--------------------


사용기술 
---------------------

-MVVM(Modle+View+ViewModel)        

-DataBinding      

-ROOM      

-MediaPlayer      

-Retrofit      

-Java      

-YouTube API   


소스에 대한 설명
--------------------

### 1.Data
##### local-Dao: Room의 쿼리 구현
##### local-Model: 각 데이터를 저장 할 모델구현(User, Music, YouTube, BackGround- 추후 개발 예정)   

##### Remote-api: Retrofit으로 통신하기 위한 레트로핏 객체 생성 과정들과 메서드들
##### Remote-Repository: Retrofit과 통신하여 데이터를 가져와 뷰모델에 데이터를 주기위한 과정   


### 2.Receiver   
##### MusicReceiver- 음악재생중 이어폰이나 헤드폰이 빠졌을 때 음악 멈춤을 위한 리시버
##### PlayerReceiver- NotificationPlayer구현을 위한 리시버


### 3.Service
##### MusicApplication, MusicService, MusicServiceImpl- 미디어 플레이어 백그라운드 재생을 위한 서비스 컴포넌트
##### PlayerCallHelper- 오디오 포커스를 위한 클래스


### 4.UI
##### Activity- 각 액티비티 구성, SplashActivity에서 시작하여 MusicActivity로 Intent하고 Fragment3가지를 탭 화면 구성으로 사용 하였다.   
##### adapter- 리사이클러뷰로 화면 구성을 위한 adapter   
##### fragment- 음악 목록, 유튜브 목록, 프로필, 음악추가에 대한 각 프래그먼트


### 5.ViewModel
##### 각 모델에 대한 뷰모델이 있고 이 곳에 데이터를 저장하여 UI, 기능에 필요한 데이터를 뷰모델에서 불러와 사용 하였다.   


### 6.Utils
##### ActivityUtils- 메인 액티비티에서 프래그먼트 변환을 위하여 사용한 유틸리티
##### Binding- 이미지에 대한 유틸리티
##### ItemOffDecoration- 리사이클러뷰 아이템에 사용한 카드뷰에 대한 간격을 주기 위한 유틸리티
##### SecondUtils- 음악시간에 대한 정보를 가져와 1000분의 1로 나누어주고 60초가 넘기면 1로 바꾸어주는 유틸리티
##### ToastUtils- Toast에 대한 메서드 사용의 코드 길이를 조금이라도 줄이기 위해 사용한 유틸리티


 

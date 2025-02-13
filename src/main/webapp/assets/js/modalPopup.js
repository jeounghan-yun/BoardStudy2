function Confirm(message, callback){
    //대화 상자를 html요소로 생성(동적)
    var confirmBox = document.createElement('div');
    confirmBox.setAttribute('class', 'confirm-box');
    confirmBox.innerHTML = '<p>' + message + '</p><button class="confirm">확인</button><button class="cancel">취소</button>';

    //html 요소를 body 요소의 하위 요소로 추가
    document.body.appendChild(confirmBox);

    //모달창의 위치 중앙 설정
    confirmBox.style.position  = 'fixed';
    confirmBox.style.top       = '50%';
    confirmBox.style.left      = '50%';
    confirmBox.style.transform = 'translate(-50%, -50%)';
    confirmBox.style.zIndex    = 999;

    //배경을 회색으로 덮어서 모달 창을 띄웠을 때 다른 요소들의 클릭을 할 수 없도록 제어
    var overlay = document.createElement('div');
    overlay.setAttribute('class', 'overlay');
    document.body.appendChild(overlay);
    overlay.style.position        = 'fixed';
    overlay.style.top             = '0'
    overlay.style.left            = '0'
    overlay.style.width           = '100%'
    overlay.style.height          = '100%'
    overlay.style.backgroundColor = 'rgba(0,0,0,0.5)';
    overlay.style.zIndex          = '998'

    var removeAlert = function (){
        document.body.removeChild(confirmBox);
        document.body.removeChild(overlay);
        window.removeEventListener('keydown', handleKeyDown);
    };

    var handleKeyDown = function (){
        event.preventDefault();
    }

    return new Promise(function (resolve){
        //확인 버튼을 클릭 이벤트
        var confirmButton = document.querySelector('.confirm');
        confirmButton.addEventListener('click', function (){
            //확인 버튼을 눌렀을 때 resolve 메서드 호출
            setTimeout(function (){
                resolve(true);
            }, 100);
        });

        //취소 버튼을 클릭 이벤트
        var cancelButton = document.querySelector('.cancel');
        cancelButton.addEventListener('click', function (){
            //취소 버튼 클릭 시 resolve 메서드 호출
            removeAlert();
            setTimeout(function (){
                resolve(false);
            }, 100);
            return false;
        });

        //회색 영역을 클릭 시 창 닫기 이벤트 추가
        overlay.addEventListener('click', function (){
            removeAlert();
        });

        //키를 누를 시 기본 동작 막기
        window.addEventListener('keydown', handleKeyDown());
    });
}
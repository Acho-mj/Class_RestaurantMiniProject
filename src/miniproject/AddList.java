package miniproject;

public class AddList {
	/* 테이블 추가 + 삭제 */
	// 현재 사용가능한 테이블 목록
	// 수용인원
	// 테이블 번호 -> 즉, UI에서 테이블을 추가하는 과정 만들어야함
	// 프로그램에서 직접 값을 넣는 것은 아니다. -> 테이블클래스 생성자에서 new 생성
	// 이미 존재하는 테이블일 경우 다시 입력받아야함
	
	// 테이블도 삭제하는 것도 추가해야됨
	// 존재하지 않는 테이블을 삭제할 경우 다시 입력받아야함 
	
	// 이미 사용한 테이블 목록을 제외시켜야함
	// 사용가능한 테이블 번호가 아닐경우 예외처리 
	// 테이블 이름을 String으로 선언(int X)
	
	// equals 를 테이블 클래스에 재정의해주면 테이블 객체끼리 비교하는게 가능해짐
	
	
	/* 관리자 클래스 */
	// 메뉴 관리
	// 테이블 관리
	
	/* 레스토랑 클래스 */
	// search 관련 함수는 private으로 선언(테이블 검색)
	
	
	// 테이블 입력 -> 존재하는 테이블인지 확인 
	// -> 주문 내역 존재하는지 확인 -> 주문 내역 존재하면 영수증 출력하기
	
	// 테이블은 전체 페이 ( 테이블 하나당 얼마인지)
	// order는 각각 메뉴 페이 (객체 당 얼마인지)
}

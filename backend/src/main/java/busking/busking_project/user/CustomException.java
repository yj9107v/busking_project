package busking.busking_project.user;

// ✅ 사용자 정의 예외 클래스: 런타임 예외 기반
public class CustomException extends RuntimeException {

    // 예외 구분을 위한 에러 코드 (예: "USER_NOT_FOUND", "INVALID_TOKEN")
    private final String errorCode;

    /**
     * ✅ 생성자
     * @param message   예외 메시지 (상위 RuntimeException에 전달됨)
     * @param errorCode 커스텀 에러 코드 (별도로 저장됨)
     */
    public CustomException(String message, String errorCode) {
        super(message); // RuntimeException 생성자 호출
        this.errorCode = errorCode; // 추가 필드 설정
    }

    /**
     * ✅ 에러 코드 반환
     * @return 설정된 에러 코드 문자열
     */
    public String getErrorCode() {
        return errorCode;
    }
}


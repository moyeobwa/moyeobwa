package momo.app.dto;

public interface OAuth2Response {
    String getProvider(); //제공자 (google)
    String getProviderId(); //제공자에서 발급해주는 아이디(번호)
    String getEmail(); //이메일
    String getName(); //사용자 실명 (설정한 이름)
}

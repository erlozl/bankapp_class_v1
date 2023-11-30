package com.tenco.bankapp.dto.response;
//{"id":3202844698,"connected_at":"2023-11-30T02:20:26Z","properties":{"nickname":"이은지","profile_image":"http://k.kakaocdn.net/dn/1G9kp/btsAot8liOn/8CWudi3uy07rvFNUkk3ER0/img_640x640.jpg","thumbnail_image":"http://k.kakaocdn.net/dn/1G9kp/btsAot8liOn/8CWudi3uy07rvFNUkk3ER0/img_110x110.jpg"},"kakao_account":{"profile_nickname_needs_agreement":false,"profile_image_needs_agreement":false,"profile":{"nickname":"이은지","thumbnail_image_url":"http://k.kakaocdn.net/dn/1G9kp/btsAot8liOn/8CWudi3uy07rvFNUkk3ER0/img_110x110.jpg","profile_image_url":"http://k.kakaocdn.net/dn/1G9kp/btsAot8liOn/8CWudi3uy07rvFNUkk3ER0/img_640x640.jpg","is_default_image":true}}}

import java.util.Properties;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoProfile {
	
	private Long id;
	private String connected_at;
	private Properties properties;

	@Getter
	@Setter
	public static class Properties {
		private String nickname;
		private String profile_image;
		private String thumbnail_image;
		
	}
	
}

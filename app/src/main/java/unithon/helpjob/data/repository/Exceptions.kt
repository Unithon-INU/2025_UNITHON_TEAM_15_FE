package unithon.helpjob.data.repository

// 닉네임 중복 예외
class NicknameDuplicateException : Exception("이미 등록된 닉네임입니다.")

// 인증 관련 예외
class UnauthorizedException : Exception("인증이 필요합니다.")

// 잘못된 요청 예외
class BadRequestException(message: String) : Exception(message)
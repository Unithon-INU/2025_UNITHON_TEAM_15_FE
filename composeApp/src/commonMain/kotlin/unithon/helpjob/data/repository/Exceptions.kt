package unithon.helpjob.data.repository

// 닉네임 중복 예외
class NicknameDuplicateException : Exception("이미 등록된 닉네임입니다.")

// 인증 관련 예외
class UnauthorizedException : Exception("인증이 필요합니다.")

// 잘못된 요청 예외
class BadRequestException(message: String) : Exception(message)

// 🆕 로그인 관련 예외
class EmailNotFoundException : Exception("해당 회원이 존재하지 않습니다.")

class WrongPasswordException : Exception("비밀번호가 일치하지 않습니다.")

// 🆕 이메일 인증 관련 예외
class EmailAlreadyInUseException : Exception("이미 존재하는 회원입니다.")

class EmailVerificationFailedException : Exception("인증코드가 유효하지 않습니다.")

class EmailCodeExpiredException : Exception("인증코드가 만료되었습니다.")

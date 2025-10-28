## 🌿 브랜치 전략

### 네이밍 규칙
- `main`: 프로덕션
- `kmp/*`: KMP 마이그레이션
- `refactor/*`: 리팩토링
- `feature/*`: 새 기능
- `hotfix/*`: 긴급 수정

### 워크플로우
1. `git checkout -b [타입]/[이름]`
2. 작업 후 PR → main
3. 머지 후 브랜치 삭제
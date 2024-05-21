# AKA - 알고리즘 비교분석기 (Algorithm Comparison Analyzer)

## 요약
이 프로그램은 같은 알고리즘 문제에 대한 두 가지의 코드를 각 테스트 케이스에 대해 컴파일, 빌드, 실행하여 결과와 수행 시간을 비교합니다.

## 의의
- 백준과 같은 프로그램에는 테스트 케이스가 충분히 주어지지 않아 나의 코드가 왜 틀렸는지 알지 못하는 경우가 많은데, 이 도구를 사용하면 정답 코드와 비교하여 다수의 테스트 케이스에 대해 비교가 가능합니다.
- 정답 코드라도 수행 시간을 비교함으로써 우수한 알고리즘을 선정할 수 있습니다.

## 사용 환경
이 도구는 C++, Python, Java를 대상으로 합니다.
각 소스 코드를 사용하기 위해서는 g++, Python 3, JDK가 설치되어 있어야 합니다.
각 소스 코드 언어에 맞는 컴파일러, 인터프리터가 존재하고 환경 변수 설정이 되어 있어야 합니다.
그렇지 않다면 settings/config 파일에서 컴파일러 경로 설정이 가능합니다.
또한 Mac, Linux, Unix 환경을 대상으로 개발되었습니다.

## 사용 방법
1) 테스트 케이스 파일 작성
   각 케이스는 `#case`로 시작합니다.
   파일 맨 하단에는 `#end`가 위치합니다.

2) 알고리즘 비교
   프로그램을 실행한 후 명령어를 입력합니다.
```
aca "소스코드1경로" "소스코드2경로" [옵션]
```

- `-o` 결과 저장 파일
- `-t` 테스트 케이스 파일 경로

테스트 파일 경로는 settings/config 파일 내부에서 직접 수정할 수도 있습니다.

`-o`를 입력하지 않으면 터미널에 결과가 출력됩니다.
`-t`를 입력하지 않으면 settings/config 파일에 명시된 경로로 수행됩니다.

## 데모
1) 문제: https://www.acmicpc.net/problem/2458
2) 테스트케이스
(settings/test_case)
```
#case
6 6
1 5
3 4
5 4
4 2
4 6
5 2
#case
6 7
1 3
1 5
3 4
5 4
4 2
4 6
5 2
#end
```
3) 소스코드
- A.java
  <img width="832" alt="스크린샷 2024-05-22 오전 3 08 47" src="https://github.com/GoByeonghu/ACA/assets/92240138/674fabd7-f9f2-4103-91e1-a497e08dd95d">
- C.py
  <img width="888" alt="스크린샷 2024-05-22 오전 3 09 31" src="https://github.com/GoByeonghu/ACA/assets/92240138/f84fc0ba-7d2a-440c-8379-6c63ba9e17f3">
4) 파일저장으로 실행
```
aca userData/A.java userData/C.py -o settings/report -t settings/test_case
```
![file](https://github.com/GoByeonghu/ACA/assets/92240138/957243d2-54c2-4dd8-8bdb-d581d3667648)
       
6) 터미널 출력으로 실행
```
aca userData/A.java userData/C.py -t settings/test_case
```
![terminal](https://github.com/GoByeonghu/ACA/assets/92240138/d48aff5e-f4e2-4869-9a08-e8bc925bf89f)
       
7) 결과
```
--------------------------------------
A = userData/A.java
B = userData/C.py
Test Case = settings/test_case
--------------------------------------
#CASE 1
INPUT
6 6
1 5
3 4
5 4
4 2
4 6
5 2
OUTPUT
A과 B이 동일합니다.
A
1
Execution Time: 28 ms
Memory Usage: 0 bytes
B
1
Execution Time: 22 ms
Memory Usage: 0 bytes


#CASE 2
INPUT
6 7
1 3
1 5
3 4
5 4
4 2
4 6
5 2
OUTPUT
A과 B이 동일합니다.
A
2
Execution Time: 27 ms
Memory Usage: 0 bytes
B
2
Execution Time: 14 ms
Memory Usage: 0 bytes
```


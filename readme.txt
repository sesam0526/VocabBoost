Main.java에서 실행하면 됨.

Intellij에서 DB Browser로 MySQL을 사용해서 프로젝트를 진행함.
첨부파일에 backup.sql이라는 mysql 백업파일을 준비함.
혹시 백업파일에 문제가 생길 경우를 대비해 최종문서 중 Design 작성 문서에 MySQL에서 만든 Tables 설정을 사진으로 제공함. 아래는 데이터베이스에 대한 접속 권한과 연결 정보임.

DB_URL = "jdbc:mysql://127.0.0.1/vocabboost";
USERNAME = "root";
PASSWORD = "0000";

mysql> SELECT USER(), CURRENT_USER();
+----------------+----------------+
| USER()         | CURRENT_USER() |
+----------------+----------------+
| root@localhost | root@localhost |
+----------------+----------------+

mysql> SHOW VARIABLES LIKE 'port';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| port          | 3306  |
+---------------+-------+

mysql> SHOW VARIABLES LIKE 'hostname';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| hostname      | PC    |
+---------------+-------+


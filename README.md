# Hướng dẫn làm bài

1. Clone Branch này về

```shell
git clone --recurse-submodules -b template --single-branch https://github.com/thnhmai06/OOP-ProgramAnalysis.git
```
**_**Optional:**_** Đổi tên Branch để khi update lên repo sẽ chạy CI (repo của bạn)
```shell
cd OOPProgramAnalysis
git checkout template
git branch -m main
```

2. Viết code của bạn ở class [Week10](./src/main/java/Week10.java)

3. Chạy tests để kiểm tra

```shell
./gradlew test
```

---------------------------------------------

# Ghi chú:

Đây là bài `Phân tích chương trình` (Week10) được lấy từ [OASIS UET](https://oasis.uet.vnu.edu.vn/).

Test cases đã được tinh chỉnh lại để phù hợp với repository thực tế.

**Vui lòng chỉ sử dụng với mục đích học hỏi, luyện tập và tham khảo, không copy paste để biến nó thành solution của mình
dưới
mọi hình thức. Mục tiêu của bài này để giúp bạn luyện tập và nâng cao kĩ năng lập trình, không phải để lấy điểm qua
môn. Mình không chịu trách nhiệm cho bất kì hành vi không mong muốn nào được nêu trên.**

---------------------------------------------

# Đề bài:

## Phân tích chương trình

[Java Collection Framework](https://docs.oracle.com/javase/tutorial/collections/index.html) gồm những lớp thư viện Java
giúp bạn lập trình dễ dàng và nhanh gọn hơn. Dưới đây giới thiệu sơ qua **List** và **String**, bạn tự tìm hiểu các lớp
khác ở link trên.

* **List**
  List Interface là một loại Interface Collection. Các phần tử của List Interface được sắp xếp có thứ tự và giá trị của
  các phần tử này có thể trùng nhau. Các phần tử trong List có thể được truy cập, thêm, sửa hoặc xóa thông qua vị trí
  của nó trong danh sách, và phần tử đầu tiên trong List sẽ có chỉ số là 0. Chi tiết về các phương thức của List có thể
  xem tại
  đây: [https://docs.oracle.com/javase/8/docs/api/java/util/List.html](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)

  ```java
      List<String> list = new ArrayList<String>();
      list.add("An");
      list.add("Nguyen");
      list.add("Tung");
      System.out.println(list.get(2)); // Tung
      list.add(1,"Binh");
      System.out.println(list.get(2)); // Nguyen
  ```

* **String**
  Chuỗi (String) trong Java cung cấp nhiều khái niệm đa dạng giúp ta thao tác và xử lý với chuỗi như so sánh, cắt, nối,
  tìm độ dài, thay thế, tìm chuỗi con,… Trong Java, về cơ bản chuỗi là một đối tượng mà biểu diễn dãy các giá trị char.
  Chuỗi (String) trong Java là không thể thay đổi (immutable), ví dụ: nó không thể bị thay đổi nhưng sẽ có một instance
  được tạo. Tuy nhiên, nếu muốn sử dụng các lớp mà có thể thay đổi, có thể lựa chọn sử dụng các lớp StringBuffer và
  StringBuilder. Chi tiết về các phương thức của String có thể xem tại
  đây: [https://docs.oracle.com/javase/8/docs/api/java/lang/String.html](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)

  ```java
      String a = new String("abc");
      String b = "abc";
      System.out.println(a == b); // false
      System.out.println(a.equals(b)); // true
      System.out.println(a.length()); // 3
      System.out.println(a + b); // abcabc
      a = " demo_trim ";

      System.out.println("before trim:" + a + "!"); 
      // before trim: demo_trim !

      System.out.println("after trim:" + a.trim() + "!"); 
      // after trim:demo_trim!
  ```

**Yêu cầu:**

Trong bài tập này, bạn sẽ được làm quen với công việc cơ bản của hướng nghiên
cứu [Phân tích chương trình](https://en.wikipedia.org/wiki/Static_program_analysis). Đây là một bài tập khó và tiêu tốn
thời gian, vì thế bạn cần thật kiên nhẫn khi làm bài tập này.

Đề bài: Áp dụng các phép biến đổi xâu, các cấu trúc dữ liệu của Java Collection Framework, hãy tạo lớp có tên **Week10**
và cài đặt phương thức ```List<String> getAllFunctions(String fileContent)``` để lấy toàn bộ chữ ký các phương thức
`static` (method signature) trong một tệp cho trước qua các phép xử lý xâu, trong đó *fileContent* là nội dung tệp bạn
cần xử lý. Mỗi một phần tử String trong đối tượng trả về lưu mã nguồn phương thức static tìm được.

Định dạng chữ ký phương thức được quy định theo định dạng:

```
methodName(parameter1_type,parameter2_type,...)
```

Chú ý là parameter1\_type, parameter2\_type,... phải
là [Fully Qualified Type Name](https://en.wikipedia.org/wiki/Fully_qualified_name)

Dưới đây là một số ví dụ:

```java
public static void sayHello() {
}
```

=> `sayHello()`

```java
public static int add(int a, int b) {
}
```

=> `add(int,int)`

```java
public static void printMessage(String message) {
}
```

=> `printMessage(java.lang.String)`

```java
public static void printMessages(String[] messages) {
}
```

=> `printMessages(java.lang.String[])`

```java
import java.io.File;

public static String readFile(File file) {
}
```

=> `readFile(java.io.File)`

```java
// tệp này có 2 lớp
package net.bqc.utils;

class Message {
}

public static void printMessage(Message message) {
}
```

=> `printMessage(net.bqc.utils.Message)`

```java
// tệp này có 2 lớp
package net.bqc.utils;

import java.util.List;

class Message {
}

public static void printMessages(List<Message> messages) {
}
```

=> `printMessages(java.util.List<net.bqc.utils.Message>)`

---------------------------------------------

Tập dữ liệu được sử dụng để làm testcase được lấy từ hai repository dưới đây:

* [Nordstrom/Java-Utils](https://github.com/thnhmai06/Nordstrom-Java-Utils)
* [RKumsher/utils](https://github.com/RKumsher/utils)

Dưới đây là một số Expected Output cho một số trường hợp trích xuất từ hai repository ở trên:

* **[VolumeInfo.java](https://github.com/thnhmai06/Nordstrom-Java-Utils/blob/master/src/main/java/com/nordstrom/common/file/VolumeInfo.java)**

```
 [getVolumeProps(), getVolumeProps(java.io.InputStream)]
```

* **[RandomNumberUtils.java](https://github.com/RKumsher/utils/blob/master/src/main/java/com/github/rkumsher/number/RandomNumberUtils.java)**

```
[randomInt(), randomInt(int,int), randomIntLessThan(int), randomLong(), randomPositiveLong(), randomNegativeLong(), randomLong(long,long), randomLongGreaterThan(long), randomLongLessThan(long), randomDouble(), randomPositiveDouble(), randomNegativeDouble(), randomDouble(double,double), randomDoubleGreaterThan(double), randomDoubleLessThan(double)]
```

* **[DatabaseUtils.java](https://github.com/thnhmai06/Nordstrom-Java-Utils/blob/master/src/main/java/com/nordstrom/common/jdbc/DatabaseUtils.java)**

```
[update(com.nordstrom.common.jdbc.DatabaseUtils.QueryAPI,java.lang.Object), getInt(com.nordstrom.common.jdbc.DatabaseUtils.QueryAPI,java.lang.Object), getString(com.nordstrom.common.jdbc.DatabaseUtils.QueryAPI,java.lang.Object), getResultPackage(com.nordstrom.common.jdbc.DatabaseUtils.QueryAPI,java.lang.Object), executeQuery(java.lang.Class<?>,com.nordstrom.common.jdbc.DatabaseUtils.QueryAPI,java.lang.Object), executeQuery(java.lang.Class<?>,java.lang.String,java.lang.String,java.lang.Object), getInt(com.nordstrom.common.jdbc.DatabaseUtils.SProcAPI,java.lang.Object), getString(com.nordstrom.common.jdbc.DatabaseUtils.SProcAPI,java.lang.Object), getResultPackage(com.nordstrom.common.jdbc.DatabaseUtils.SProcAPI,java.lang.Object), executeStoredProcedure(java.lang.Class<?>,com.nordstrom.common.jdbc.DatabaseUtils.SProcAPI,java.lang.Object), executeStoredProcedure(java.lang.Class<?>,java.lang.String,java.lang.String,com.nordstrom.common.jdbc.Param), executeStatement(java.lang.Class<?>,java.sql.Connection,java.sql.PreparedStatement), getConnection(java.lang.String)]
```

*Chú ý trường hợp phương thức đặt trong comment.*

### Source code mẫu :

- [Mẫu project đầy đủ, bao gồm cả test cases](https://github.com/thnhmai06/OOPProgramAnalysis/tree/template) (Recommend)
- [Mẫu chỉ 1 class, được cung cấp ban đầu](https://github.com/oasis-homework/BaiTapHangTuan/blob/main/Week10.java)

**Good luck!**
# Crud-Operations-With-Custom-Api-Response

> [!NOTE]
> ### In this Api we customize Api Response.

## Tech Stack
- Java
- Spring Framework
- Spring Boot
- Spring Data JPA
- lombok
- Validation
- MySQL
- Postman
- Swagger UI

## Modules
* Student Modules

## Documentation
Swagger UI Documentation - http://localhost:8080/swagger-ui/

## Installation & Run
Before running the API server, you should update the database config inside the application.properties file.
Update the port number, username and password as per your local database config.
    
```
spring.datasource.url=jdbc:mysql://localhost:3306/mydb;
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
```

## API Root Endpoint

```
https://localhost:8080/
http://localhost:8080/swagger-ui/
user this data for checking purpose.
```
## Step To Be Followed
> 1. Create Rest Api will return to Student Details.
>    
>    **Project Documentation**
>    - **Entity** - Student (class)
>    - **Payload** - StudentDto, ApiResponceDto (class)
>    - **Repository** - StudentRepository (interface)
>    - **Service** - StudentService (interface), StudentServiceImpl (class)
>    - **Controller** - StudentController (Class)
>    - **Global Exception** - GlobalException, UserNotFoundException (class)
>      
> 2. Run the application and get All API Response similar types.

## Important Dependency to be used
1. For rest api
``` 
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
 </dependency>
```

2. For Getter and Setter
``` 
 <dependency>
     <groupId>org.projectlombok</groupId>
     <artifactId>lombok</artifactId>
     <optional>true</optional>
 </dependency>
```

3. For Validation
``` 
 <dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

4. For Swagger
``` 
<dependency>
	<groupId>org.springdoc</groupId>
	<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
	<version>2.3.0</version>
</dependency>
```

5. For Mysql and JPA
``` 
<dependency>
	<groupId>com.mysql</groupId>
	<artifactId>mysql-connector-j</artifactId>
	<scope>runtime</scope>
	</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

## Create Student class in Entity Package.
```
package com.it.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "FullName", nullable = false)
    private String fullName;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "Mobile", nullable = false, unique = true)
    private String mobile;

    @Column(name = "Age", nullable = false)
    private Long age;

    @Column(name = "Gender", nullable = false)
    private String gender;

    @CreationTimestamp
    @Column(name = "CreateDate", updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "UpdateDate", insertable = false)
    private LocalDateTime updateDate;

}
```

## Create StudentRepository interface in repository package.

```package com.it.Repository;

import com.it.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);


    List<Student> findByFullNameContainingIgnoreCase(String fullname);

}
```

## Create StudentService interface and StudentServiceImpl class in Service package.

### *StudentService*
```
public interface StudentService {

    // create Student
    public StudentDto createStudent(StudentDto dto);

    // get All student Details
    public List<StudentDto> getAllStudent();

    // get All student in page Format
    public Page<StudentDto> getAllStudentPage(Pageable pageable);

    // get single Student details
    public StudentDto getStudent(Long id);

    // Update Student Details
    public StudentDto updateStudent(Long id, StudentDto dto);

    // Delete Student Details
    public StudentDto deleteStudent(Long id);

    // Search Student details using name
    public List<StudentDto> searchByName(String name);

}
```

### *StudentService*
```
@Service
public class StudentServiceImpl implements StudentService {
    private StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // map to dto
    private StudentDto mapTODto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setFullName(student.getFullName());
        dto.setEmail(student.getEmail());
        dto.setPassword(student.getPassword());
        dto.setMobile(student.getMobile());
        dto.setGender(student.getGender());
        dto.setAge(student.getAge());
        return dto;
    }

    // map to Entity
    private Student mapToEntity(StudentDto dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setPassword(dto.getPassword());
        student.setMobile(dto.getMobile());
        student.setGender(dto.getGender());
        student.setAge(dto.getAge());
        return student;
    }

    @Override
    public StudentDto createStudent(StudentDto dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setPassword(dto.getPassword());
        student.setGender(dto.getGender());
        student.setAge(dto.getAge());
        student.setMobile(dto.getMobile());

        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("User email already exist.");
        }
        if (studentRepository.existsByMobile(dto.getMobile())) {
            throw new IllegalArgumentException("User Mobile no. already exist.");
        }

        Student saveStudent = studentRepository.save(student);
        StudentDto studentDto = mapTODto(saveStudent);
        return studentDto;
    }

    @Override
    public List<StudentDto> getAllStudent() {
        List<Student> studentList = studentRepository.findAll();
        List<StudentDto> studentDtoList = studentList.stream().map(this::mapTODto).collect(Collectors.toUnmodifiableList());
        return studentDtoList;
    }

    @Override
    public Page<StudentDto> getAllStudentPage(Pageable pageable) {
        Page<Student> studentPage = studentRepository.findAll(pageable);
        List<StudentDto> studentDtoList = studentPage.getContent().stream().map(this::mapTODto).collect(Collectors.toList());
        return new PageImpl<>(studentDtoList, pageable, studentPage.getTotalPages());
    }

    @Override
    public StudentDto getStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Student Not Found By Id"));
        StudentDto studentDto = mapTODto(student);
        return studentDto;
    }

    @Override
    public StudentDto updateStudent(Long id, StudentDto dto) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Student Not Found By Id"));
        student.setId(id);
        student.setFullName(dto.getFullName());
        student.setEmail(dto.getEmail());
        student.setPassword(dto.getPassword());
        student.setGender(dto.getGender());
        student.setAge(dto.getAge());
        student.setMobile(dto.getMobile());
        Student save = studentRepository.save(student);
        StudentDto studentDto = mapTODto(save);
        return studentDto;
    }

    @Override
    public StudentDto deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Student Not Found By Id"));
        studentRepository.deleteById(student.getId());
        StudentDto studentDto = mapTODto(student);
        return studentDto;
    }

    @Override
    public List<StudentDto> searchByName(String name) {
        List<Student> studentList = studentRepository.findByFullNameContainingIgnoreCase(name);
        List<StudentDto> studentDtoList = studentList.stream().map(this::mapTODto).collect(Collectors.toUnmodifiableList());
        return studentDtoList;
    }
}
```

##  Create ApiResponse and StudentDto class inside the Payload Package.

### *StudentDto* 
```
@Data
public class StudentDto {

    private Long id;

    @NotBlank(message = "This Filed is required")
    private String fullName;

    @NotBlank(message = "This Filed is required")
    @Email(message = "Enter the Valid mail id")
    private String email;

    @NotBlank(message = "This Filed is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Password must have of minimum 8 Characters and at least one uppercase letter, one lowercase letter, one number and one special character")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull(message = "This Filed is required")
    @Size(min = 10, max = 10, message = "size must be 10 digits.")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits Only.")
    private String mobile;

    @NotNull(message = "This Filed is required")
    @Min(18)
    @Max(60)
    private Long age;

    @NotBlank(message = "This Filed is required")
    private String gender;
}

```
### *ApiResponseDto* 
```
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String massage;
    private T Data;

}
```


### *Create GlobalException class and UserNotFoundException class inside the GlobalException Package.* 

### *GlobalException* 

```
@RestControllerAdvice
public class GlobalExceptionHandler {

    // General error handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneral(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // UserNotFoundException handler
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFound(UserNotFoundException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // IllegalArgumentException Handler
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> IllegalArgumentExceptionHandle(IllegalArgumentException ex) {
        ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // MethodArgumentNotValidException handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handlerInvalidArgument(MethodArgumentNotValidException ex) {

        Map<String, String> errorMsg = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errorMsg.put(error.getField(), error.getDefaultMessage()));
        ApiResponse<Object> response = new ApiResponse<>(false, "Something went wrong", errorMsg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
```

### *UserNotFoundException* 
```
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
```


## Configure **_Swagger Definition_** to use Api Documentation and all Controller Documentation.

### *Swegger Defination*
```
// configure swagger OpenAPIDefinition
@OpenAPIDefinition(
		info = @Info(
				title = "Crud Operations with custom Api Response",
				version = "1.0",
				description = "In this Api we customize Api Response.",
				contact = @Contact(
						name = "Kundan Kumar Chourasiya",
						email = "mailmekundanchourasiya@gmail.com"
				)
		),
		servers = @Server(
				url = "http://localhost:8080",
				description = "Crud Operations with custom Api Response url"
		)
)
```


### Following pictures will help to understand flow of API

### *Swagger*

![image](https://github.com/user-attachments/assets/ab1b5567-08de-4994-b51a-704b97401a3f)

### *PostMan Test Cases*

Url - http://localhost:8080/student/create
![image](https://github.com/user-attachments/assets/23227c38-af02-4a09-ae6f-b5f7a58a9720)

Url - http://localhost:8080/student/search?name=g
Url - http://localhost:8080/student/search?name=Armando
![image](https://github.com/user-attachments/assets/bae60f1d-eb55-45f1-849c-db2e78fee760)

Url - http://localhost:8080/student/all-student
![image](https://github.com/user-attachments/assets/f65d7d34-8d3c-48a4-9699-524741de0bee)

Url - http://localhost:8080/student/get/4
![image](https://github.com/user-attachments/assets/b6f69bc6-8a69-470c-9298-34cf62bb68d5)

Url - http://localhost:8080/student/update/6
![image](https://github.com/user-attachments/assets/f094ac96-78bb-495a-a857-32906a3be89c)

Url - http://localhost:8080/student/delete/6
![image](https://github.com/user-attachments/assets/aa86df71-5145-45fd-8ad6-11eb81a176db)

URL :  http://localhost:8080/student/allstudent
URL :  http://localhost:8080/student/allstudent?page=0
URL : http://localhost:8080/student/allstudent?page=0&size=2

![image](https://github.com/user-attachments/assets/965fa88d-de0d-4c9f-a323-2b73d9190cc4)


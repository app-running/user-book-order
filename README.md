Small REST Api application to allow Users Borrow and return book.

📚 UserBookOrder API
A RESTful Spring Boot application for borrowing and returning books, built with:

✅ Spring Boot
✅ Spring Data JPA
✅ PostgreSQL
✅ REST API principles
✅ JUnit + Mockito

All REST API : 

| Service                                         | Method | Request                                             |
| :---                                            | :---   | :---                                                |
| **User API**                                    | :---   | :---                                                |
| Create User                                     | POST   |  "/user/create"                                     |
| **Book API**                                    | :---   | :---                                                |
| Get All Book                                    | POST   |  "/book"                                            |
| Create Book                                     | GET    |  "/book/addBook"                                    |
| **UserBookOrder API**                           | :---   | :---                                                |
| Borrow book by User                             | POST   | "/borrow-book"                                      |
| Return book by User                             | POST   | "/return-book"                                      |
| Get all borrow books by User Id with pagination | GET    | "/list/borrow-book?user={userId}&page=1&perPage=10" |
| Get all return books by User Id with pagination | GET    | "/list/return-book?user={userId}&page=1&perPage=10" |
| Get all borrow books with pagination            | GET    | "/list/borrow-book?page=1&perPage=10"               |
| Get all return books with pagination            | GET    | "/list/return-book?page=1&perPage=10"               |

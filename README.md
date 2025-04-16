Small REST Api application to allow Users Borrow and return book.

📚 UserBookOrder API
A RESTful Spring Boot application for borrowing and returning books, built with:

✅ Spring Boot
✅ Spring Data JPA
✅ PostgreSQL
✅ REST API principles
✅ JUnit + Mockito

All REST API : 
User: 
1. Create User                                     : POST "/user/create"

Book: 
1. Create Book                                     : POST "/book/addBook"
2. Get All Book                                    : GET  "/book"
   
UserBookOrder:
1. Borrow book by User                             : POST "/borrow-book"
2. Return book by User                             : POST "/return-book"
3. Get all borrow books by User Id with pagination : GET "/list/borrow-book?user={userId}&page=1&perPage=10"
4. Get all return books by User Id with pagination : GET "/list/return-book?user={userId}&page=1&perPage=10"
5. Get all borrow books with pagination            : GET "/list/borrow-book?page=1&perPage=10"
4. Get all return books with pagination            : GET "/list/return-book?page=1&perPage=10"


   


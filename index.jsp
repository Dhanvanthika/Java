<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <title>E-Library</title>
    <link rel="stylesheet" href="style.css">
</head>

<body>

    <div class="container">

        <h1>E-Library</h1>
        <p>Search and explore available books</p>

        <form>
            <label>Book Title:</label>
            <input type="text" name="bookTitle" placeholder="Enter book title">

            <label>Category:</label>
            <select name="category">
                <option>All</option>
                <option>Programming</option>
                <option>Database</option>
                <option>Networking</option>
                <option>Fiction</option>
            </select>

            <input type="submit" value="Search">
        </form>

        <h2>Available Books</h2>

        <table>
            <tr>
                <th>Book ID</th>
                <th>Title</th>
                <th>Author</th>
                <th>Category</th>
            </tr>

            <tr>
                <td>101</td>
                <td>Java Programming</td>
                <td>Herbert Schildt</td>
                <td>Programming</td>
            </tr>

            <tr>
                <td>102</td>
                <td>Database Systems</td>
                <td>Raghu Ramakrishnan</td>
                <td>Database</td>
            </tr>

            <tr>
                <td>103</td>
                <td>Computer Networks</td>
                <td>Andrew S. Tanenbaum</td>
                <td>Networking</td>
            </tr>

        </table>

    </div>

</body>
</html>
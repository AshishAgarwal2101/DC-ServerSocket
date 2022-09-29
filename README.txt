## Name:
Ashish Agarwal

## Assignment Name:
Programming Assignment 1

## Description:
* This project is used to demonstrate usage of server sockets using java to implement a functional HTTP based web server.
* A Web server listens for connections bound to a specific port.
* Clients request for files and server responds with the file contents if it exists and proper permissions are set.

## List of submitted files:
The project has the following file structure:
* model:
    * HttpRequest.java: This class is used to parse incoming HTTP request and extract protocol, version, headers, and other metadata, along with the requested file.
    * HttpResponse.java: This class is used to store the response parameters like HTTP version, headers, body and other such information. It is also used to build the final response that could be returned to the client.
* handler:
    * ServerHandler.java: This is the class that is used to execute the newly spawned thread when an incoming request comes in. It parses the request, fetches the necessary files and returns the response to the client.
* Server.java: This is the entry point. It has the main class and is used to run the server.
* server.sh: Shell script that takes in document_root and port number and runs the java based web server.
* webpage: This directory is the root directory for the server files. It contains the index.html file.
* screenshots: This directory contains screenshots of demonstrations of running the webserver in a browser. It demonstrates following examples:
    * homepage.jpg: It shows the homepage in the browser, i.e. "http://localhost:80". The server returned a status code of 200 with the necessary contents.
    * visit_page.jpg: It shows the page after clicking on the "VISIT" button at the homepage. The server returned a status code of 200 with necessary contents.
    * unavailable_file.jpg: It shows the result when we try to access a file that is not available in the server, i.e. "somefile.txt". The server returns a status code of 404.

## Explanation of the program:
* The Server class waits for requests from clients.
* Whenever a new request comes in, it spaws a new thread and let ServerHandler handle the request.
* The ServerHandler does the following operations:
    * Read Request: It parses the request to find the requested protocol, version, metadata and the requested resource. If HTTP protocol is not used or if a request method other than the GET request is used, a 400 status code response is returned.
    * Write Response: It tries to fetch the requested resource. If the resource is not found, a 404 status code response is returned. If the resource doesn't have the necessary read permission set, a 403 status code response is returned. Finally, if the resource is found, the resource with a status code 200 is returned.
    * At any point, if there is any server error, it returns a 500 status code to the client.

## Instruction for running the program
To run this, you need a UNIX based system.

Step 1: Open Terminal and point to the code directory.
Step 2: Use the following command:
./server.sh -document_root "<path to your server files>" -port <server port number>

Example:
./server.sh -document_root "/usr/data/webpage" -port 80

Explanation:
The above command will compile the java files and execute the class file with the parameters passed by the user.

## Important Notes:
* Only the following links are available in the server - directory "webpage". Other links redirect to *sjsu.edu*:
    * "VISIT" link in the menubar.
    * "APPLY" link in the menubar.
    * "GIVE" link in the menubar.
    * "Explore Themed Events" link.
    * "View All Facts and Accomplishments" link.
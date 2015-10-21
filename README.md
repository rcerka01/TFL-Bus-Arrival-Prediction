# TFL-Bus-Arrival-Prediction
Real Time TFL Bus Arrival Prediction

Test project to get along with Scala web development on Play2.
A special attention is to make up side down in non blocking way. Beginig with Slick3 all requests
is made via Futures Promises in non blocking way
Project uses Dedbolt for security, along with the Slick3 for Persistance over MySql database.
The Authentacion and Authorisation process is hand made.

The purpose for this project is to return bus arrival time predictions from TFL API.
The composition of actions follow one page principle. Most of tasks is asynchronos.

The data is returned from TFL via Json array, filtered and then passed to screen 
via JS JQuery asynchronous request by using Json structure.

The programm is letting you:<br>
1) Authenticate user<br>
2) Get real time bus arrival time predictions<br>
3) Save user related bus routes<br>
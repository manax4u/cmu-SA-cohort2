
/******************************************************************************************************************
* File:REST.js
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2018 Carnegie Mellon University
* Versions:
*   1.0 February 2018 - Initial write of assignment 3 for 2018 architectures course(ajl).
*
* Description: This module provides the restful webservices for the Server.js Node server. This module contains GET,
* and POST services.
*
* Parameters:
*   router - this is the URL from the client
*   connection - this is the connection to the database
*   md5 - This is the md5 hashing/parser... included by convention, but not really used
*
* Internal Methods:
*   router.get("/"... - returns the system version information
*   router.get("/orders"... - returns a listing of everything in the ws_orderinfo database
*   router.get("/orders/:order_id"... - returns the data associated with order_id
*   router.post("/order?"... - adds the new customer data into the ws_orderinfo database
*   router.delete("/orders/:order_id"... - deletes order with order_id
*   router.get("/authenticate/:userid"... - validates user credentials and returns user role
*
* External Dependencies: mysql
*
******************************************************************************************************************/

var mysql   = require("mysql");     //Database

function REST_ROUTER(router,connection) {
    var self = this;
    self.handleRoutes(router,connection);
}

// Here is where we define the routes. Essentially a route is a path taken through the code dependent upon the
// contents of the URL

REST_ROUTER.prototype.handleRoutes= function(router,connection) {

    // GET with no specifier - returns system version information
    // req paramdter is the request object
    // res parameter is the response object

    router.get("/",function(req,res){
        console.log('Request Type:', req.method, ', Request: ', '/');
        res.json({"Message":"Orders Webservices Server Version 1.0"});
    });

    // GET for /orders specifier - returns all orders currently stored in the database
    // req paramdter is the request object
    // res parameter is the response object

    router.get("/orders",function(req,res){
        console.log('Request Type: ', req.method, ', Request: ', '/orders');
        console.log("Getting all orders from database..." );
        var query = "SELECT * FROM ??";
        var table = ["orders"];
        query = mysql.format(query,table);
        console.log('SQL query to get all orders: ', query);
        connection.query(query,function(err,rows){
            if(err) {
                console.error('Error getting orders from database: ', err);
                res.json({"Error" : true, "Message" : "Error executing MySQL query"});
            } else {
                console.log('GET Orders from database successfull...');
                res.json({"Error" : false, "Message" : "Success", "Orders" : rows});
            }
        });
    });

    // GET for /orders/order id specifier - returns the order for the provided order ID
    // req paramdter is the request object
    // res parameter is the response object

    router.get("/orders/:order_id",function(req,res){
        const orderId = req.params.order_id;
        console.log('Request Type: ', req.method, ', Request: ', '/orders/', orderId);
        console.log("Getting order ID: ", orderId );
        var query = "SELECT * FROM ?? WHERE ??=?";
        var table = ["orders","order_id",orderId];
        query = mysql.format(query,table);
        console.log('SQL query to get order ID ', orderId, ' is: ', query);
        connection.query(query,function(err,rows){
            if(err) {
                console.error('Error getting order from database: ', err);
                res.json({"Error" : true, "Message" : "Error executing MySQL query"});
            } else {
                console.log('GET Order from database successfull...');
                res.json({"Error" : false, "Message" : "Success", "Users" : rows});
            }
        });
    });

    // POST for /orders?order_date&first_name&last_name&address&phone - adds order
    // req paramdter is the request object - note to get parameters (eg. stuff afer the '?') you must use req.body.param
    // res parameter is the response object

    router.post("/orders",function(req,res){
        //console.log("url:", req.url);
        console.log('Request Type: ', req.method, ', Request: ', '/orders');
        console.log('Request Body: ', req.body);
        console.log("Adding to orders table ", req.body.order_date,",",req.body.first_name,",",req.body.last_name,",",req.body.address,",",req.body.phone);
        var query = "INSERT INTO ??(??,??,??,??,??) VALUES (?,?,?,?,?)";
        var table = ["orders","order_date","first_name","last_name","address","phone",req.body.order_date,req.body.first_name,req.body.last_name,req.body.address,req.body.phone];
        query = mysql.format(query,table);
        console.log('SQL query to add order: ', query);
        connection.query(query,function(err,rows){
            if(err) {
                console.error('Error adding order into database: ', err);
                res.json({"Error" : true, "Message" : "Error executing MySQL query"});
            } else {
                console.log('Adding Order into database successfull...');
                res.json({"Error" : false, "Message" : "User Added !"});
            }
        });
    });

    // Delete for /orders/order id specifier - deletes the order for the provided order ID
    // req parameter is the request object
    // res parameter is the response object

    router.delete("/orders/:order_id",function(req,res){
        const orderId = req.params.order_id;
        console.log('Request Type: ', req.method, ', Request: ', '/orders/', orderId);
        console.log("Deleting order ID: ", req.params.order_id );
        var query = "DELETE FROM ?? WHERE ??=?";
        var table = ["orders","order_id",orderId];
        query = mysql.format(query,table);
        console.log('SQL query to delete order ID ', orderId, ' is: ', query);
        connection.query(query,function(err,rows){
            if(err) {
                console.error('Error deleting order from database: ', err);
                res.json({"Error" : true, "Message" : "Error executing MySQL query"});
            } else {
                console.log('DELETE Order from database successfull...');
                res.json({"Error" : false, "Message" : "Success", "Users" : rows});
            }
        });
    });

    // Delete for /orders/order id specifier - deletes the order for the provided order ID
    // req paramdter is the request object
    // res parameter is the response object

    router.get("/authenticate/:userid",function(req,res){
        const userId = req.params.userid;
        console.log('Request Type: ', req.method, ', Request: ', '/authenticate/', userId);
        console.log("Validating credentials: ", req.params.userid );
        var query = "SELECT password FROM ?? WHERE ??=?";;
        var table = ["userCredential","user_name",req.params.userid];
        query = mysql.format(query,table);
        console.log('SQL query to delete order ID ', orderId, ' is: ', query);
        connection.query(query,function(err,rows){
            if(err) {
                console.error('Error validating credentials from database: ', err);
                res.json({"Error" : true, "Message" : "Error executing MySQL query"});
            } else {
                console.log('Validating credentials from database successfull...');
                res.json({"Error" : false, "Message" : "Success", "password" : rows[0].password});
            }
        });
    });
}

// The next line just makes this module available... think of it as a kind package statement in Java
module.exports = REST_ROUTER;

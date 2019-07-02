var asRepo = "2"; // ASpace repo

//var baseURL = "http://159.203.105.249:8089/"; // ASpace test server URL
// var baseURL = "http://localhost:8089" // ASpace URL
var baseURL="https://iasc.mit.edu/adml/api"
//var baseURL="https://emmastaff-lib.mit.edu/api"; // production IASC url

var serverURL="https://iasc.mit.edu/adml/token"; // change when changing IPs
//var serverURL="http://localhost:8080/adml/token"; // for local machine testing
var token;

$(document).ready(function() {

    // test call
    // console.log(getPropertyRecursive( myobj, 'test' ));

    $.get( serverURL, function(data){
        token = data;
        console.log("Got token from ADML server OK.");
        // console.log(token);
    }).fail(function() {
        alert("Error doing a lookup.");
    });

    $('#find_in_as').click(function () {
        //e.preventDefault();
        refid = $('#refID').val();
        var params = "ref_id[]=" + refid;
        getResults(params, refid);
        //getLocation(params, refid); // sample test
    });

});


// Handle click event on "Find in ArchivesSpace" button
$('#find_in_as').on('click', function(e) {
    e.preventDefault();
    refid = $('#refID').val();
    var params = "ref_id[]=" + refid;
    getResults(params, refid);
});


// Fetches archival object ID by ref ID
function getResults(data, refid) {
    $.ajax({
        type: "GET",
        dataType: "json",
        beforeSend: function(request) {
            console.log("Sending request");
            request.setRequestHeader("X-ArchivesSpace-Session", token);
            //request.setRequestHeader('Accept','application/json; charset=utf-8');
        },
        url: baseURL + "/repositories/"+ asRepo +"/find_by_id/archival_objects?",
        data: data,
        success: function(results) {
            console.log("Fetched archival object by id");
            if (results["archival_objects"].length < 1) {
                console.log("Sorry, I couldn't find anything for: " + refid);
            } else {
                getData(results["archival_objects"][0]["ref"]);
            }
        },  error: function (xhr, status) {
            console.log('Ajax error = ' + xhr.statusText);
        }
    });
}

// Fetches component and resource data and box info or a particular ref ID
function getData(uri) {
    $.ajax({
        type: "GET",
        dataType: "json",
        beforeSend: function(request) {
            console.log("Sending request to:" + baseURL + uri);
            request.setRequestHeader("X-ArchivesSpace-Session", token);
            request.setRequestHeader('Accept','application/json; charset=utf-8');
        },
        url: baseURL + uri,
        success: function(data) {
            // alert("ok: get data call")
            console.log("Entering getData()");
            if (data["jsonmodel_type"] == "resource") {
                console.log("This is a resource");
                $('#resource').val(data["title"] + ' (' + data["id_0"] + ')');
            } else if (data["jsonmodel_type"] == "archival_object") {
                console.log("This is an archival object");
                $('#component').val(data['display_string'])
                console.log("Getting info regarding resource ref");
                getData(data["resource"]["ref"]);
                //console.log(getPropertyRecursive( data['instances'][0], 'indicator_1' ));
                //var abc = getPropertyRecursive(data['instances'][0], 'indicator_1');
                //console.log(abc);
                //onsole.log(abc[0]);
                //console.log(abc[0]["value"]); // box
                //$('#box').val(abc[0]["value"]);
            } else if (data["type"] == "adml_resource") {
                // osm: just the title
                console.log(data.toString());
                $('#resource').val(data["title"]);
            } else {
                console.log("Unknown json model type");
            }
        }, error: function (xhr, status) {
            console.log('Error in getData = ' + xhr.statusText);
            console.log(status);
            console.log(xhr.responseText);

        }
    });
}

function traverse(obj,func, parent) {
    for (i in obj){
        func.apply(this,[i,obj[i],parent]);
        if (obj[i] instanceof Object && !(obj[i] instanceof Array)) {
            traverse(obj[i],func, i);
        }
    }
}

function getPropertyRecursive(obj, property){
    var acc = [];
    traverse(obj, function(key, value, parent){
        if(key === property){
            acc.push({parent: parent, value: value});
        }
    });
    return acc;
}

// Sample test
function getLocation(data, refid) {
    $.ajax({
        type: "GET",
        dataType: "json",
        beforeSend: function(request) {
            console.log("Sending request for location");
            request.setRequestHeader("X-ArchivesSpace-Session", token);
        },
        url: baseURL + "/locations/1",
        data: data,
        success: function(results) {
            alert("ok: get results");
            console.log(results);
        },  error: function (xhr, status) {
            console.log('Ajax error = ' + xhr.statusText);
        }
    });
}
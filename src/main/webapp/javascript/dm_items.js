// Note: This JS file is temporary and will be replaced when we have a different API.

var asRepo = "1"; // ASpace repo

//var baseURL = "http://159.203.105.249:8089/"; // ASpace URL
// var baseURL = "http://localhost:8089" // ASpace URL
var baseURL="http://emmastaff-lib.mit.edu:8089";

var serverURL="https://iasc.mit.edu/adml/token"; // change when changing IPs
//var serverURL="http://localhost:8080/adml/token";
var token;

$(document).ready(function() {

    // test call
    // console.log(getPropertyRecursive( myobj, 'test' ));

    $.get( serverURL, function(data){
        token = data;
        console.log("Got token OK");
    }).fail(function() {
        alert("Error doing a lookup.");
    });

    $('#find_in_as').click(function () {
        //e.preventDefault();
        console.log("loaded");
        refid = $('#refID').val();
        var params = "ref_id[]=" + refid;
        getResults(params, refid);
    });

});


// Handle click event on Find in ArchivesSpace button and delegate to getResults function
$('#find_in_as').on('click', function(e) {
    e.preventDefault();
    refid = $('#refID').val();
    var params = "ref_id[]=" + refid;
    getResults(params, refid);
});


// Get JSON results from the AS endpoint
function getResults(data, refid) {
    $.ajax({
        type: "GET",
        dataType: "json",
        beforeSend: function(request) {
            request.setRequestHeader("X-ArchivesSpace-Session", token);
        },
        url: baseURL + "/repositories/"+ asRepo +"/find_by_id/archival_objects?",
        data: data,
        success: function(results) {
            // alert("ok: get results");
            if (results["archival_objects"].length < 1) {
                console.log("Sorry, I couldn't find anything for " + refid);
            } else {
                //alert(results["archival_objects"][0]["ref"]);
                getData(results["archival_objects"][0]["ref"]);
            }
        }
    });
}

// Fetches JSON from an ArchivesSpace URI
function getData(uri) {
    $.ajax({
        type: "GET",
        dataType: "json",
        beforeSend: function(request) {
            request.setRequestHeader("X-ArchivesSpace-Session", token);
        },
        url: baseURL + uri,
        success: function(data) {
            // alert("ok: get data call")
            if (data["jsonmodel_type"] == "resource") {
                $('#resource').val(data["title"] + ' (' + data["id_0"] + ')');
            } else if (data["jsonmodel_type"] == "archival_object") {
                // alert("archival object")
                $('#component').val(data['display_string'])
                getData(data["resource"]["ref"]);
                console.log(getPropertyRecursive( data['instances'][0], 'indicator_1' ));
                var abc = getPropertyRecursive(data['instances'][0], 'indicator_1');
                console.log(abc[0]["value"]); // box
                $('#box').val(abc[0]["value"]);
            }
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

// test json object:
var myobj = {
    boo :'1',
    bo1 :'1',
    bo2 : {
        boo :'2',
        test :'2',
        bo3 : {
            bar1: '3',
            bar2: '3',
            bar3: '3',
            test: '3',
            bar4: '3',
        }
    },
    boo :'1',
};
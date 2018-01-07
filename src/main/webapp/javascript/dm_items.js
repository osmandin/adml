// Generate date variables
var date = new Date();
var day = date.getDate();
var month = date.getMonth() + 1;
var year = date.getFullYear();
if (month < 10) month = "0" + month;
if (day < 10) day = "0" + day;
var today = year + "-" + month + "-" + day;
var asRepo = "2";
var baseURL = "http://159.203.105.249:8089/"
var token="33463110b65c72d369fa1e8b92af5877bf8e20f05940b1515f8597fb1903791d"


$(document).ready(function() {
    $('#find_in_as').click(function () {
        //e.preventDefault();
        // alert("test");
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
            alert("ok: get results");
            if (results["archival_objects"].length < 1) {
                console.log("Sorry, I couldn't find anything for " + refid);
            } else {
                alert(results["archival_objects"][0]["ref"]);
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
            alert("ok: get data call")
            if (data["jsonmodel_type"] == "resource") {
                $('#resource').val(data["title"] + ' (' + data["id_0"] + ')');
            } else if (data["jsonmodel_type"] == "archival_object") {
                // alert("archival object")
                $('#component').val(data['display_string'])
                getData(data["resource"]["ref"]);
            }
        }
    });
}

function validateForm() {
    var isValid = true;
    $('.dm_items .form-control[required]').each(function() {
        if ( $(this).val() == ''  ) {
            isValid = false;
        }
    });
    return isValid;
}

// Populate auto id field with unique ID
function generateId() {
    if ($('#dm_item_auto_id').val() == "") {
        var auto_id = createId();
        $('#dm_item_auto_id').val(auto_id);
    }
}

// Generate a unique ID
function createId() {
    var text = "";
    var possible = "abcdefghijklmnopqrstuvwxyz0123456789";
    for( var i=0; i < 10; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));
    return text;
}
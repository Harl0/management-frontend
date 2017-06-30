var RESULTS_TABLE = "clientsTable";
var table = document.getElementById(RESULTS_TABLE);

$(document).ready(function () {

    //call reloadClients
    reloadClients = function () {
        // $.getJSON({
        //     type: "GET",
        //     dataType: "json",
        //     url: "/client/getClients",
        //     success: function (data, textStatus, jqXHR) {
        //         if (jqXHR.status == "200") {
        //             console.log("Calling /client");
        //         } else processClientsFailure();
        //     },
        //     error: function (errorThrown) {
        //         processClientsFailure();
        //     }
        // })
        $.getJSON('http://mgmt-www/client/getClients', function (data) {
            console.log(data);
            console.log(data.clients);

            for (var i = 0; i < data.length; i++) {
                // tableBuilder(dsesData.stats[i]);
console.log("Working" + i)
            }

            // var row = table.insertRow(0);
            // var entity = row.insertCell(0);
            // entity.innerHTML = "Hi"
        });
    };


    reloadClients();

});
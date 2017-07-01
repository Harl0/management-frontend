// var RESULTS_TABLE = "clientsTable";
// var table = document.getElementById(RESULTS_TABLE);

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
            // console.log(data.clients[0]._id);
            console.log(data.clients);

            for (var i = 0; i < data.clients.length; i++) {
                // tableBuilder(dsesData.stats[i]);

                console.log("Working" + i)
            }
            // var row = table.insertRow(-1);
            // var id = row.insertCell(0);
            // var name = row.insertCell(1);
            // var secret = row.insertCell(2);
            // id.innerHTML = data.clients._id;
            var table = document.getElementById("myTable");

            var header = table.createTHead();
            var headerRow = header.insertRow(0);
            var ID = headerRow.insertCell(0);
            var clientName = headerRow.insertCell(1);
            ID.innerHTML = "Client ID".bold();
            clientName.innerHTML = "Client Name".bold();

            var row = table.insertRow(1);
            var id = row.insertCell(0);
            var name = row.insertCell(1);
            id.innerHTML = data.clients[0].clientId;
            name.innerHTML = data.clients[0].clientName;
        });
    };


    reloadClients();

});
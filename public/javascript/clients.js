// var RESULTS_TABLE = "clientsTable";
// var table = document.getElementById(RESULTS_TABLE);
var table = document.getElementById("myTable");

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
            console.log(data.clients);
            processResults(data);
        });

        processResults = function (data) {
            initTable();
            for (var i = 0; i < data.clients.length; i++) {
                var table = document.getElementById("myTable");

                console.log("Working" + i);
                var row = table.insertRow(1);
                var id = row.insertCell(0);
                var name = row.insertCell(1);

                id.innerHTML = data.clients[i].clientId;
                name.innerHTML = data.clients[i].clientName;
            }
        };

        initTable = function () {
            var table = document.getElementById("myTable");

            var header = table.createTHead();
            var headerRow = header.insertRow(0);
            var ID = headerRow.insertCell(0);
            var clientName = headerRow.insertCell(1);
            ID.innerHTML = "Client ID".bold();
            clientName.innerHTML = "Client Name".bold();
        }
    };

    reloadClients();
});
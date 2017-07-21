$(document).ready(function () {
    $("#delete-all").change(function () {
        $("input[value='delete']:checkbox").prop('checked', $(this).prop("checked"));
    });

    $('#delete-submit').click(function () {
        var $inputs = $("td input[name='delete']:checked");
        var count = $inputs.length;
        if (confirm('Are you sure you want to delete ' + count + ' collector(s)')) {
            $inputs.each(function () {
                var id = $(this).parent().siblings().first().text();
                $.ajax({
                    contentType: 'application/json',
                    method: 'GET',
                    url: '/management/collector/delete?_id=' + id,
                    data: JSON.stringify([id]),
                    success: function (data, status) {
                        window.location.replace('/management/collector/list');
                    },
                    error: function (req, status, error) {
                        console.log(status + error);
                    }
                });
            });
        }
    });
});

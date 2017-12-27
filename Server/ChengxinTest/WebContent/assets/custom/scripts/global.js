var uploadPath = "/ChengxinTest";
var STATS_UPDATE_FREQ = 5000;

bootbox.setLocale('zh_CN');
$.extend(true, $.fn.dataTable.defaults, {
    "serverSide": true,
    "bStateSave": true,
    "deferRender": true,
    // set the initial value
    "processing": true,
    "pageLength": 10,
    "autoWidth":true,
    "lengthChange": true,
    "lengthMenu": [
        [10, 20, 50,  -1],
        [10, 20, 50, "全部"] // change per page values here
    ],
    "pagingType": "bootstrap_full_number",
    "language": {
        "aria": {
            "sortAscending": ": activate to sort column ascending",
            "sortDescending": ": activate to sort column descending"
        },
        "loadingRecords": "数据下载中...",
        "processing": "数据下载中...",
        "emptyTable": "没有数据",
        "info": "一共_TOTAL_个中 _START_ ~ _END_",
        "infoEmpty": "没有数据",
        "infoFiltered": "(一共 _MAX_个 数据中 搜索)",
        "lengthMenu": "显示个数 _MENU_",
        "search": "搜索:",
        "zeroRecords": "没有数据",
        "paginate": {
            "previous":"上一个",
            "next": "下一个",
            "last": "最后",
            "first": "第一个"
        }
    }
});

if ($.fn.datepicker) {

    if ($.fn.datepicker.dates) {
        $.fn.datepicker.dates.zh_CN = {
        		days:["星期日","星期一","星期二","星期三","星期四","星期五","星期六","星期日"],
        		daysShort:["周日","周一","周二","周三","周四","周五","周六","周日"],
        		daysMin:["日","一","二","三","四","五","六","日"],
        		months:["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
        		monthsShort:["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
        		today:"今日",
        		format:"yyyy年mm月dd日",
        		weekStart:1,
        		clear:"清空"
        };
    }

    defaults = {
        language:'zh_CN',
        format: 'yyyy-mm-dd',
        todayHighlight: true,
        orientation: "left",
        clearBtn: true,
        autoclose: true
	};

    $.extend($.fn.datepicker.defaults, defaults);

    
}

function isNumber(val) {
	return /^[-]?\d+$/.test(val);
}

function getParameterByName( name )
{
  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
  var regexS = "[\\?&]"+name+"=([^&#]*)";
  var regex = new RegExp( regexS );
  var results = regex.exec( window.location.href );
  if( results == null )
    return "";
  else
  {
    if ((results[1].indexOf('?'))>0)
        return decodeURIComponent(results[1].substring(0,results[1].indexOf('?')).replace(/\+/g, " "));
    else
        return decodeURIComponent(results[1].replace(/\+/g, " "));
  }
}

function checkImgUrl(){
	$('img').each(function(index , resp){
		$(resp).error(function(){
			$(resp).attr('src' , 'assets/custom/img/no_image.png');
		});
	});
}

$.fn.formToSearchArray = function() {
    var search_param = {};
    var form = $(this);
    $('textarea.form-filter, select.form-filter, input.form-filter:not([type="radio"],[type="checkbox"])', form).each(function() {
        if ($(this).val() != '') {
            var obj = {};
            obj[$(this).attr("name")] = $(this).val();
            if (!search_param["equal_param"]) {
                search_param["equal_param"] = [];
            }
            search_param["equal_param"].push(obj);
        }
    });
    $('textarea.form-like-filter, select.form-like-filter, input.form-like-filter:not([type="radio"],[type="checkbox"])', form).each(function() {
        if ($(this).val() != '') {
            var obj = {};
            obj[$(this).attr("name")] = $(this).val();
            if (!search_param["like_param"]) {
                search_param["like_param"] = [];
            }
            search_param["like_param"].push(obj);
        }
    });
    $('div.form-date-filter:not([type="radio"],[type="checkbox"])').each(function() {
        var fromVal = $(this).children('input[name=from]').val();
        var toVal = $(this).children('input[name=to]').val();
        if(fromVal != '' || toVal != '') {
            var obj = {};
            if(fromVal !='') {
                obj['from'] = fromVal;
            }
            if(toVal != '') {
                obj['to'] = toVal;
            }
            var obj2 = {};
            obj2[$(this).attr("name")] = obj;
            if(!search_param["date_search_param"]) {
            	search_param["date_search_param"] = [];
            }
            search_param["date_search_param"].push(obj2);
        }
    });
    return search_param;
}

$('#global-modal').on('loaded.bs.modal', function() {
    if ($('modal_width').length > 0) {
        $('#global-modal').children('div.modal-dialog').css('width', $('modal_width').attr('val'));
    }
});

function setTimer() {
	  setTimeout(function() {
		 refreshNoticeCnt();
		 setTimer();
	  }, 3000);
}
function refreshNoticeCnt() {
	  $.ajax({
			type : "POST",
			url : "notice.html?pAct=getNewNoticeCnt",
			data : {},
			success : function(res) {
				if (res.retcode == 200) {
					if(res.newNoticeCnt > 0) {
						var html = "<i class='icon-bell' style='padding-right: 30px'></i><span class='badge badge-danger' style='right: 3px;'>" + res.newNoticeCnt + "</span>";
						$("#notice_cnt").html(html);	
					}
					else {
						$("#notice_cnt").html("");
					}
				} else {
					toastr['error'](res.msg);
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				/* bootbox.alert("发生错误！请您确认网络状态"); */
				/*console.log("发生错误！请您确认网络状态");*/
				location.href = "login.html";
			}
		});
	  
}
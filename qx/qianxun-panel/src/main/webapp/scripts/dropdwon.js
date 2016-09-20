function dropdown(id,place,code) {
	$("#" + id).cascadingDropdown(
		{
			selectBoxes : [
					{
						selector : '.step1',
						source : function(request, response) {
							$.getJSON('/region/api', request, function(data) {
								var defaultCode = code - code % 10000;
								response($.map(data, function(item, index) {
									if (item.code == defaultCode) {
										return {
											label : item.name,
											value : item.code,
											selected : true
										};
									} else {
										return {
											label : item.name,
											value : item.code,
											selected : false
										};
									}
								}));
							});
						},
						onChange : function(event, value, requiredValues) {
							$(".step3").hide();
							if (value == '')
								value = 0;
							$("#code").val(value);
							$("#"+place).val(value);
						}
					},
					{
						selector : '.step2',
						requires : [ '.step1' ],
						source : function(request, response) {
							$.getJSON('/region/api', request, function(data) {
								var defaultCode = code - code % 100;
								var value = code - code % 10000;
								if (value == 110000 || value == 120000
										|| value == 310000 || value == 500000) {
									defaultCode = code;
								}

								response($.map(data, function(item, index) {
									if (item.code == defaultCode) {
										return {
											label : item.name,
											value : item.code,
											selected : true
										};
									} else {
										return {
											label : item.name,
											value : item.code,
											selected : false
										};
									}
								}));
							});
						},
						onChange : function(event, code, requiredValues) {
							value = code - code % 10000;
							if (value == 110000 || value == 120000
									|| value == 310000 || value == 500000) {
								$("#code").val(code);
								$("#"+place).val(code);
							} else {
								$(".step3").show();
								$("#code").val(code);
								$("#"+place).val(code);
							}
						}
					}, {
						selector : '.step3',
						requires : [ '.step1', '.step2' ],
						requireAll : true,
						source : function(request, response) {
							$.getJSON('/region/api', request, function(data) {
								response($.map(data, function(item, index) {
									if (item.code == code) {
										return {
											label : item.name,
											value : item.code,
											selected : true
										};
									} else {
										return {
											label : item.name,
											value : item.code,
											selected : false
										};
									}
								}));
							});
						},
						onChange : function(event, code, requiredValues) {
							$("#code").val(code);
							$("#"+place).val(code);
						}
					} ]
		});
}
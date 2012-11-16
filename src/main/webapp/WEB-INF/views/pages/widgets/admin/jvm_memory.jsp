<script>
	$(function(){
		$('#gc_btn').click(function(){
			$.post('${ctx}/widgets/admin/gc', function(data) {
				$('#used_bar').attr('width', data.usedPercent*80/100+'%');
				$('#used_val').text(data.used);
				$('#free_bar').attr('width', data.freePercent*80/100+'%');
				$('#free_val').text(data.free);
				$('#total_bar').attr('width', data.totalPercent*80/100+'%');
				$('#total_val').text(data.total);
				$('#max_val').text(data.max);
				jprompt.alert('Run successfully.');
			});
		});
		$('#gc_btn').button();
	});
</script>
<table class="bar_chart" cellspacing="0" cellpadding="0">
	<caption align="top">JVM Memory</caption>
	<tr>
		<th scope="col" style="width:70px;"><span class="auraltext">Memory</span> </th>
		<th scope="col"><span class="auraltext">Amount</span> </th>
	</tr>
	<tr>
		<td class="first">Used</td>
		<td class="value first">
			<img id="used_bar" src="${ctx }/resources/images/chart/horizontal_bar/bar.png" width="${usedPercent*80/100 }%" height="16" />
			<span id="used_val">${used }</span>&nbsp;MB
		</td>
	</tr>
	<tr>
		<td class="first">Free</td>
		<td class="value first">
			<img id="free_bar" src="${ctx }/resources/images/chart/horizontal_bar/bar.png" width="${freePercent*80/100 }%" height="16" />
			<span id="free_val">${free }</span>&nbsp;MB
		</td>
	</tr>
	<tr>
		<td class="first">Total</td>
		<td class="value first">
			<img id="total_bar" src="${ctx }/resources/images/chart/horizontal_bar/bar.png" width="${totalPercent*80/100 }%" height="16" />
			<span id="total_val">${total }</span>&nbsp;MB
		</td>
	</tr>
	<tr>
		<td class="first">Max</td>
		<td class="value first">
			<img id="max_bar" src="${ctx }/resources/images/chart/horizontal_bar/bar.png" width="80%" height="16" />
			<span id="max_val">${max }</span>&nbsp;MB
		</td>
	</tr>
</table>
<footer>
	<div class="submit_link">
		<input type="button" id="gc_btn" value="System.gc()"/>
	</div>
</footer>



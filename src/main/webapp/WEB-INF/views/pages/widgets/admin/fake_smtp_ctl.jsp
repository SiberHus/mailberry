<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
	function setInput(enabled){
		if(enabled){
			$('#smtp_port').removeAttr('disabled');
			$('#chance_of_error').removeAttr('disabled');
		}else{
			$('#smtp_port').attr('disabled', 'disabled');
			$('#chance_of_error').attr('disabled', 'disabled');
		}
	}
	$(function(){
		$('#command_btn').click(function(){
			jprompt.confirm('Start/Stop operation will reset the counter. Do you want to continue?',function(){
				$.post('${ctx}/widgets/admin/fake-smtp-server', $('#main_form').serialize(),function(data){
					if(data.error){
						jprompt.alert(data.errorDetail);
					}else{
						$('#successes').text('0');
						$('#errors').text('0');
						if($('#command').val()=='Start'){
							setInput(false);
							$('#command').val('Stop');
							$('#command_btn').val('Stop');
							$('#status').text('Started');
						}else{
							setInput(true);
							$('#command').val('Start');
							$('#command_btn').val('Start');
							$('#status').text('Stopped');
						}
					}
				});
			});
		});
		$('#command_btn').button();
		//updateUi();
	});
</script>
<form id="main_form">
<table>
	<tbody>
		<tr>
			<td colspan="2" style="text-align: center;border-style: dashed;border-width: thin; border-color: gray;">
				<div style="margin: 5px 5px;">
					<span style="color: blue; font-weight: bold;">
						Sucesses : &nbsp;<span id="successes">${successes }</span>
					</span>
					&nbsp;&nbsp;&nbsp;
					<span style="color: red; font-weight: bold;">
						Errors : &nbsp;<span id="errors">${errors }</span>
					</span>
				</div>
			</td>
		</tr>
		<tr>
			<td>Status</td>
			<td>
				<span id="status" style="font-weight: bold;">
					<c:if test="${stopped }">
						Stopped
						<c:set var="command" value="Start"/>
						<script>setInput(true);</script>
					</c:if>
					<c:if test="${not stopped }">
						Started
						<c:set var="command" value="Stop"/>
						<script>setInput(false);</script>
					</c:if>
				</span>
				<input type="hidden" id="command" name="command" value="${command }"/>
			</td>
		</tr>
		<tr>
			<td>Port</td>
			<td>
				<input type="text" id="smtp_port" name="port" value="${port }"/>
			</td>
		</tr>
		<tr>
			<td>Chance of Error</td>
			<td>
				<input type="text" id="chance_of_error" name="chanceOfError" value="${chanceOfError }"/> %
			</td>
		</tr>
		<tr>
			<td colspan="2" style="text-align:center; border-top-style: dashed;border-top-width: thin; border-top-color: gray;">
				<input type="button" id="command_btn" value="${command }"
					style="width:200px; margin-top:10px;"/>
			</td>
		</tr>
	</tbody>
</table>
</form>
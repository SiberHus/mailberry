var ids = $(this).jqGrid('getDataIDs');
for(var i=0;i < ids.length;i++){
	var cl = ids[i];
	edi = "<input type='image' class='grid_action' src='${ctx}/resources/images/icons/pencil.png' onclick=\"crud.editObject('"+cl+"');\" title='Edit'>";
	vie = "<input type='image' class='grid_action' src='${ctx}/resources/images/icons/information.png' onclick=\"crud.viewObject('"+cl+"');\" title='View'>";
	del = "<input type='image' class='grid_action' src='${ctx}/resources/images/icons/delete.png' onclick=\"crud.deleteObject('"+cl+"',function(){grid.reload();});\" title='Delete'>"; 
	$(this).jqGrid('setRowData',ids[i],{act:edi+vie+del});
}
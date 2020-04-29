<?php


function json_output($statusHeader,$response)
{
	$ci =& get_instance();
	$ci->output->set_content_type('application/json');
	$ci->output->set_status_header($statusHeader);
	$ci->output->set_output(json_encode($response));
}

function json_output1($message)
{
	$ci =& get_instance();
	$ci->output->set_content_type('application/json');
	$ci->output->set_output(json_encode($message));
}

function assign_value($var)
{
	$var=trim($var);
	if($var === '')
	{
		return null;
	}
	else
	{
		return $var;
	}
}

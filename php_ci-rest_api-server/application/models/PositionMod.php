<?php

class PositionMod extends CI_Model
{
	public function get_positions()
	{
		$query = $this->db->query("select po.*, pr.nazov as nazov2 from pozicia po join pracovisko pr on po.pracovisko=pr.id");
		return $query->result();
	}
}

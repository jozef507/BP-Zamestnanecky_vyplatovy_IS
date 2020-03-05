<?php


class PlaceMod extends CI_Model
{
	public function get_places_names()
	{
		$query = $this->db->query("select * from pracovisko");
		return $query->result();
	}

}

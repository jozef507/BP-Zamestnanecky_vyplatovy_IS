<?php


class RelationMod extends CI_Model
{
	public function get_next_demands_detail($id)
	{
		$query = $this->db->query("select * from dalsie_podmienky dup where dup.id=".$id);
		return $query->result();
	}

}

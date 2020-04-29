<?php


class YearMod extends CI_Model
{
	public function get_all_years_of_this_yearnumber($year, $relID)
	{

		$command = "select o.* from pracovny_vztah pv join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu where pv.id=".$relID." and o.rok=".$year." order by ppv.platnost_od desc";
		$query = $this->db->query($command);
		return $query->result_array();
	}
}

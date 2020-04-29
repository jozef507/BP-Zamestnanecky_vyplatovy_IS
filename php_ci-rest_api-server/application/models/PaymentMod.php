<?php


class PaymentMod extends CI_Model
{
	public function get_payments_last_month()
	{
		$prev_month_date = date("Y-m-d", strtotime("first day of previous month"));
		$prev_month_year = date("Y", strtotime("first day of previous month"));
		$prev_month_month = strval(intval(date("m", strtotime("first day of previous month"))));

		$prev_month_date = '2020-01-01';
		$prev_month_year = '2020';
		$prev_month_month = '1';

		$query = $this->db->query("select rok from odpracovany_rok group by rok order by rok");
		$count = $query->num_rows();
		$years = $query->result_array();

		$count_arr = array("pocet"=>$count, "rok"=>$prev_month_year, "mesiac"=>$prev_month_month);

		$result = array();
		array_push($result, $count_arr);
		$result = array_merge($result, $years);

		$query = $this->db->query("select p.id as p_id, p.priezvisko, p.meno, pv.id as pv_id, pv.typ, ppv.id as ppv_id, ppv.platnost_od, ppv.platnost_do, p2.id as p2_id, p2.nazov as p2_nazov, p3.id as p3_id, p3.nazov as p3_nazov, o.id as o_id, o.rok, om.id as om_id, om.poradie_mesiaca, om.je_mesiac_uzatvoreny, vp.id as vp_id, vp.* from pracujuci p join pracovny_vztah pv on p.id = pv.pracujuci join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah join pozicia p2 on ppv.pozicia = p2.id join pracovisko p3 on p2.pracovisko = p3.id left join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu left join odpracovany_mesiac om on o.id = om.odpracovany_rok left join vyplatna_paska vp on om.vyplatna_paska = vp.id where (('".$prev_month_date."' between ppv.platnost_od and ppv.platnost_do) or (ppv.platnost_od<= '".$prev_month_date."' and platnost_do is null)) and o.rok = ".$prev_month_year." and om.poradie_mesiaca = ".$prev_month_month." order by p.priezvisko, p.meno, p.id");
		$payments = $query->result_array();

		$result = array_merge($result, $payments);
		return $result;
	}

	public function get_payments_month($year, $month)
	{
		$strmonth = "";
		if(strlen($month)===1)
			$strmonth="0".$month;
		else
			$strmonth=$month;
		$date = $year."-".$strmonth."-01";

		$query = $this->db->query("select p.id as p_id, p.priezvisko, p.meno, pv.id as pv_id, pv.typ, ppv.id as ppv_id, ppv.platnost_od, ppv.platnost_do, p2.id as p2_id, p2.nazov as p2_nazov, p3.id as p3_id, p3.nazov as p3_nazov, o.id as o_id, o.rok, om.id as om_id, om.poradie_mesiaca, om.je_mesiac_uzatvoreny, vp.id as vp_id, vp.* from pracujuci p join pracovny_vztah pv on p.id = pv.pracujuci join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah join pozicia p2 on ppv.pozicia = p2.id join pracovisko p3 on p2.pracovisko = p3.id left join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu left join odpracovany_mesiac om on o.id = om.odpracovany_rok left join vyplatna_paska vp on om.vyplatna_paska = vp.id where (('".$date."' between ppv.platnost_od and ppv.platnost_do) or (ppv.platnost_od<= '".$date."' and platnost_do is null)) and o.rok = ".$year." and om.poradie_mesiaca = ".$month." order by p.priezvisko, p.meno, p.id");

		return $query->result_array();
	}

	public function get_info_for_emplyee_payment($month_id)
	{
		$query = $this->db->query("select o.id as o_id, o.rok, om.id as om_id, om.poradie_mesiaca from odpracovany_rok o join odpracovany_mesiac om on o.id = om.odpracovany_rok where om.id=".$month_id);
		$row = $query->result_array();
		$year_number=$row[0]['rok'];
		$month_number=$row[0]['poradie_mesiaca'];

		$strmonth = "";
		if(strlen($month_number)===1)
			$strmonth="0".$month_number;
		else
			$strmonth=$month_number;
		$date = $year_number."-".$strmonth."-01";

		$query = $this->db->query("select *, TIME_FORMAT(zaciatok_nocnej_prace, '%H:%i') as zaciatok_nocnej_prace, TIME_FORMAT(koniec_nocnej_prace, '%H:%i') as koniec_nocnej_prace from mzdove_konstanty mk where (('".$date."' between mk.platnost_od and mk.platnost_do) or ('".$date."'>=mk.platnost_od and mk.platnost_do is null))");
		$constants = $query->result_array();

		$query = $this->db->query("select * from minimalna_mzda mm where stupen_narocnosti = 1 and (('".$date."' between mm.platnost_od and mm.platnost_do) or ('".$date."'>=mm.platnost_od and mm.platnost_do is null))");
		$minwage = $query->result_array();

		$query = $this->db->query("select dup.id as dup_id, dup.mesto as dup_mesto, dup.ulica as dup_ulica, dup.cislo as dup_cislo, dup.*, p.id as p_id, DATE_FORMAT(p.datum_narodenia,'%d.%m.%Y') as p_datum_narodenia, p.*, pv.id as pv_id, DATE_FORMAT(pv.datum_vzniku,'%d.%m.%Y') as pv_datum_vzniku, DATE_FORMAT(pv.datum_vyprsania,'%d.%m.%Y') as pv_datum_vyprsania, pv.*, ppv.id as ppv_id, DATE_FORMAT(ppv.platnost_od,'%d.%m.%Y') as ppv_platnost_od, DATE_FORMAT(ppv.platnost_do,'%d.%m.%Y') as ppv_platnost_do, ppv.*, dp.id as dp_id, dp.*, p2.id as p2_id, p2.nazov as p2_nazov, p2.charakteristika as p2_charakteristika, p2.*, p3.id as p3_id, p3.nazov as p3_nazov, p3.*, sn.id as sn_id, sn.charakteristika as sn_charakteristika, sn.*, mm.id as mm_id, DATE_FORMAT(mm.platnost_od,'%d.%m.%Y') as mm_platnost_od, DATE_FORMAT(mm.platnost_do,'%d.%m.%Y') as mm_platnost_do, mm.*,o.id as o_id, o.*, om.id as om_id, om.* from dolezite_udaje_pracujuceho dup join pracujuci p on dup.pracujuci = p.id join pracovny_vztah pv on p.id = pv.pracujuci join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah left join dalsie_podmienky dp on ppv.dalsie_podmienky = dp.id join pozicia p2 on ppv.pozicia = p2.id join pracovisko p3 on p2.pracovisko = p3.id join pozicia_stupen_narocnosti psn on p2.id = psn.pozicia join stupen_narocnosti sn on psn.stupen_narocnosti = sn.id join minimalna_mzda mm on sn.id = mm.stupen_narocnosti join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu join odpracovany_mesiac om on o.id = om.odpracovany_rok where (om.id=".$month_id.") and (('".$date."' between dup.platnost_od and dup.platnost_do) or ('".$date."'>=dup.platnost_od and dup.platnost_do is null)) and (('".$date."' between mm.platnost_od and mm.platnost_do) or ('".$date."'>=mm.platnost_od and mm.platnost_do is null))");
		$conditions = $query->result_array();

		$query = $this->db->query("select zm.id as zm_id, zm.*, fm.id as fm_id, fm.* from odpracovany_mesiac om join odpracovany_rok o on om.odpracovany_rok = o.id join podmienky_pracovneho_vztahu ppv on o.podmienky_pracovneho_vztahu = ppv.id join zakladna_mzda zm on ppv.id = zm.podmienky_pracovneho_vztahu join forma_mzdy fm on zm.forma_mzdy = fm.id where (om.id=".$month_id.")");
		$basicwages = $query->result_array();
		$basicwages_count = $query->num_rows();


		$interesting_unclosed_months = array();
		$interesting_unclosed_months_num = 0;
		if($basicwages[0]['sposob_vyplacania']=='pravidelne')
		{
			$isRegular=true;
		}
		else
		{
			$isRegular=false;

			$con_id = $conditions[0]['ppv_id'];
			$query = $this->db->query("select o.id as o_id, o.*, om.id as om_id, om.* from odpracovany_rok o join odpracovany_mesiac om on o.id = om.odpracovany_rok where o.podmienky_pracovneho_vztahu=".$con_id." and om.je_mesiac_uzatvoreny=false order by om.id desc");
			$unclosed_months = $query->result_array();

			foreach ($unclosed_months as $row)
			{
				$year_number1=$row['rok'];
				$month_number1=$row['poradie_mesiaca'];

				$strmonth1 = "";
				if(strlen($month_number1)===1)
					$strmonth1="0".$month_number1;
				else
					$strmonth1=$month_number1;
				$date1 = $year_number1."-".$strmonth1."-01";

				if($date1<$date) {
					array_push($interesting_unclosed_months, $row);
					$interesting_unclosed_months_num++;
				}
			}
		}

		$count_arr = array("pocet_miezd"=>$basicwages_count, "pocet_neuzatvorenach_mesiacov"=>$interesting_unclosed_months_num);
		$result = array();
		array_push($result, $count_arr);
		$result = array_merge($result, $constants);
		$result = array_merge($result, $minwage);
		$result = array_merge($result, $conditions);
		$result = array_merge($result, $basicwages);
		$result = array_merge($result, $interesting_unclosed_months);
		return $result;

	}





}

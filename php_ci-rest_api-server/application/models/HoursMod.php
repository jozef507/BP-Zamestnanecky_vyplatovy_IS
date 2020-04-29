<?php


class HoursMod extends CI_Model
{
	public function get_hours($date)
	{
		if($date=='null')
		{
			$command = "select fm.id as fm_id, fm.nazov as fm_nazov, zm.id as zm_id, zm.popis as zm_popis, oh.*, oh.id as oh_id,  DATE_FORMAT(oh.datum,'%d.%m.%Y') as nice_date1, TIME_FORMAT(oh.od, '%H:%i') as nice_time1, TIME_FORMAT(oh.do, '%H:%i') as nice_time2,  om.id as om_id, om.poradie_mesiaca, orr.id as orr_id, orr.rok, ppv.id as ppv_id, pv.id as pv_id, pv.typ as pv_typ, p.id as p_id, p.meno, p.priezvisko, po.id as po_id, po.nazov as po_nazov, pr.id as pr_id, pr.nazov as pr_nazov from forma_mzdy fm join zakladna_mzda zm on fm.id = zm.forma_mzdy join odpracovane_hodiny oh on zm.id = oh.zakladna_mzda join odpracovany_mesiac om on oh.odpracovany_mesiac = om.id join odpracovany_rok orr on om.odpracovany_rok = orr.id join podmienky_pracovneho_vztahu ppv on orr.podmienky_pracovneho_vztahu = ppv.id join pracovny_vztah pv on ppv.pracovny_vztah = pv.id join pracujuci p on pv.pracujuci = p.id join pozicia po on ppv.pozicia = po.id join pracovisko pr on po.pracovisko = pr.id where oh.aktualizovane <= now() and oh.aktualizovane >= DATE_SUB(now(), INTERVAL 3 DAY) order by oh.aktualizovane desc;";
			$query = $this->db->query($command);
		}
		else
		{
			$command = "select fm.id as fm_id, fm.nazov as fm_nazov, zm.id as zm_id, zm.popis as zm_popis, oh.*, oh.id as oh_id,  DATE_FORMAT(oh.datum,'%d.%m.%Y') as nice_date1, TIME_FORMAT(oh.od, '%H:%i') as nice_time1, TIME_FORMAT(oh.do, '%H:%i') as nice_time2,  om.id as om_id, om.poradie_mesiaca, orr.id as orr_id, orr.rok, ppv.id as ppv_id, pv.id as pv_id, pv.typ as pv_typ, p.id as p_id, p.meno, p.priezvisko, po.id as po_id, po.nazov as po_nazov, pr.id as pr_id, pr.nazov as pr_nazov from forma_mzdy fm join zakladna_mzda zm on fm.id = zm.forma_mzdy join odpracovane_hodiny oh on zm.id = oh.zakladna_mzda join odpracovany_mesiac om on oh.odpracovany_mesiac = om.id join odpracovany_rok orr on om.odpracovany_rok = orr.id join podmienky_pracovneho_vztahu ppv on orr.podmienky_pracovneho_vztahu = ppv.id join pracovny_vztah pv on ppv.pracovny_vztah = pv.id join pracujuci p on pv.pracujuci = p.id join pozicia po on ppv.pozicia = po.id join pracovisko pr on po.pracovisko = pr.id where oh.datum = '".$date."' order by p_id";
			$query = $this->db->query($command);
		}

		return $query->result();
	}

	public function get_employee_hours_of_month($month_id)
	{
		$query = $this->db->query("select om.id as om_id, oh.id as oh_id, oh.*, TIME_FORMAT(oh.od, '%H:%i') as od, TIME_FORMAT(oh.do, '%H:%i') as do,zm.id as zm_id from odpracovany_mesiac om join odpracovane_hodiny oh on om.id = oh.odpracovany_mesiac join zakladna_mzda zm on oh.zakladna_mzda = zm.id join forma_mzdy fm on zm.forma_mzdy = fm.id where om.id = ".$month_id." order by zm.id, oh.datum, oh.od");
		return $query->result();
	}


	public function create_hours()
	{
		$params = $_REQUEST;
		$hrs_id = null;
		$hrs_date = $params['date'];

		if($params['from']=='NULL')
			$hrs_from=null;
		else
			$hrs_from = $params['from'];
		if($params['to']=='NULL')
			$hrs_to=null;
		else
			$hrs_to = $params['to'];
		if($params['overtime']=='NULL')
			$hrs_overtime=null;
		else
			$hrs_overtime = $params['overtime'];
		if($params['units']=='NULL')
			$hrs_units=null;
		else
			$hrs_units = $params['units'];
		if($params['part']=='NULL')
			$hrs_part=null;
		else
			$hrs_part = $params['part'];
		if($params['emergency']=='NULL')
			$hrs_emergency=null;
		else
			$hrs_emergency = $params['emergency'];

		$cons_id = $params['consid'];
		$wage_id = $params['wageid'];

		$month = intval(date("m",strtotime($hrs_date)));
		$year = intval(date("Y",strtotime($hrs_date)));

		$current_date = date("YYYY-mm-dd");

		if($hrs_date>$current_date)
			return array('status' => 403,'message' => 'Nie je mozne pridat odpracovane hodiny do buducnosti.');

		$this->db->trans_start();

			$query = $this->db->query("select om.* from podmienky_pracovneho_vztahu ppv join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu join odpracovany_mesiac om on o.id = om.odpracovany_rok where ppv.id = ".$cons_id." and o.rok= ".$year." and om.poradie_mesiaca= ".$month);
			$rows_count = $query->num_rows();

			if($rows_count==0)
			{
				$query = $this->db->query("select o.* from podmienky_pracovneho_vztahu ppv join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu where ppv.id = ".$cons_id." and o.rok= ".$year);
				$rows_count = $query->num_rows();
				if($rows_count==0)
				{
					$data = array(
						'rok' => $year,
						'podmienky_pracovneho_vztahu' => $cons_id
					);
					$this->db->insert('odpracovany_rok', $data);
					$year_id = $this->db->insert_id();
				}
				else if($rows_count==1)
				{
					$result = $query->result_array() ;
					$year_id = $result[0]["id"];
				}
				else
				{
					return array('status' => 403,'message' => 'Existuje viacero možných rokov.');
				}

				$data = array(
					'poradie_mesiaca' => $month,
					'odpracovany_rok' => $year_id
				);
				$this->db->insert('odpracovany_mesiac', $data);
				$month_id = $this->db->insert_id();
			}
			else if($rows_count==1)
			{
				$result = $query->result_array() ;
				$month_id = $result[0]["id"];
				$month_closed = $result[0]["je_mesiac_uzatvoreny"];
				if($month_closed==1)
					return array('status' => 403,'message' => 'Mesiac je uz uzatvoreny. Nie je mozne pridavar odpracovane hodiny.');
			}
			else
			{
				return array('status' => 403,'message' => 'Existuje viacero možných mesiacov.');
			}

			$data = array(
				'datum' => $hrs_date,
				'od' => $hrs_from,
				'do' => $hrs_to,
				'z_toho_nadcas' => $hrs_overtime,
				'pocet_vykonanych_jednotiek' => $hrs_units,
				'zaklad_podielovej_mzdy' => $hrs_part,
				'druh_casti_pohotovosti' => $hrs_emergency,
				'odpracovany_mesiac' => $month_id,
				'zakladna_mzda' => $wage_id
			);
			$this->db->insert('odpracovane_hodiny', $data);
			$hrs_id = $this->db->insert_id();

		if($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'hrs_id' => $hrs_id);
		}
	}

	public function update_hours()
	{
		$params = $_REQUEST;
		$hrs_id = $params['id'];
		if($params['from']=='NULL')
			$hrs_from=null;
		else
			$hrs_from = $params['from'];
		if($params['to']=='NULL')
			$hrs_to=null;
		else
			$hrs_to = $params['to'];
		if($params['overtime']=='NULL')
			$hrs_overtime=null;
		else
			$hrs_overtime = $params['overtime'];
		if($params['units']=='NULL')
			$hrs_units=null;
		else
			$hrs_units = $params['units'];
		if($params['part']=='NULL')
			$hrs_part=null;
		else
			$hrs_part = $params['part'];
		if($params['emergency']=='NULL')
			$hrs_emergency=null;
		else
			$hrs_emergency = $params['emergency'];

		$month_id = $params['omid'];



		$this->db->trans_start();

			$query = $this->db->query("select om.* from  odpracovany_mesiac om  where om.id = ".$month_id);
			$result = $query->result_array();
			if($result[0]['je_mesiac_uzatvoreny']==1)
				return array('status' => 500,'message' => 'Mesiac je uzatvoreny. Nie je mozne previest zmenu hodin.');

			$data = array(
				'od' => $hrs_from,
				'do' => $hrs_to,
				'z_toho_nadcas' => $hrs_overtime,
				'pocet_vykonanych_jednotiek' => $hrs_units,
				'zaklad_podielovej_mzdy' => $hrs_part,
				'druh_casti_pohotovosti' => $hrs_emergency
			);
			$this->db->where('id', $hrs_id)->update('odpracovane_hodiny', $data);

		if($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'hrs_id' => $hrs_id);
		}
	}

	public function delete_hours($id)
	{
		$this->db->trans_start();

			$this->db->where('id', $id)->delete('odpracovane_hodiny');

		if($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'id' => $id);
		}
	}
}

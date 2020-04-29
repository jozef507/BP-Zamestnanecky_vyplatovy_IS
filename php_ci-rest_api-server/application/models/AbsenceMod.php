<?php


class AbsenceMod extends CI_Model
{
	public function get_absences($date)
	{
		if($date=='null')
		{
			$command = "select n.*, n.id as n_id,  DATE_FORMAT(n.od,'%d.%m.%Y') as nice_date1, DATE_FORMAT(n.do,'%d.%m.%Y') as nice_date2, ppv.id as ppv_id, pv.id as pv_id, pv.typ as pv_typ, p.id as p_id, p.meno, p.priezvisko, po.id as po_id, po.nazov as po_nazov, pr.id as pr_id, pr.nazov as pr_nazov from nepritomnost n join odpracovany_mesiac_nepritomnost omn on n.id=omn.nepritomnost join odpracovany_mesiac om on omn.odpracovany_mesiac = om.id join odpracovany_rok orr on om.odpracovany_rok = orr.id join podmienky_pracovneho_vztahu ppv on orr.podmienky_pracovneho_vztahu = ppv.id join pracovny_vztah pv on ppv.pracovny_vztah = pv.id join pracujuci p on pv.pracujuci = p.id join pozicia po on ppv.pozicia = po.id join pracovisko pr on po.pracovisko = pr.id where n.aktualizovane <= now() and n.aktualizovane >= DATE_SUB(now(), INTERVAL 3 DAY)  group by n.id order by n.aktualizovane desc";
			$query = $this->db->query($command);
		}
		else
		{
			$command = "select n.*, n.id as n_id,  DATE_FORMAT(n.od,'%d.%m.%Y') as nice_date1, DATE_FORMAT(n.do,'%d.%m.%Y') as nice_date2, ppv.id as ppv_id, pv.id as pv_id, pv.typ as pv_typ, p.id as p_id, p.meno, p.priezvisko, po.id as po_id, po.nazov as po_nazov, pr.id as pr_id, pr.nazov as pr_nazov from nepritomnost n join odpracovany_mesiac_nepritomnost omn on n.id=omn.nepritomnost join odpracovany_mesiac om on omn.odpracovany_mesiac = om.id join odpracovany_rok orr on om.odpracovany_rok = orr.id join podmienky_pracovneho_vztahu ppv on orr.podmienky_pracovneho_vztahu = ppv.id join pracovny_vztah pv on ppv.pracovny_vztah = pv.id join pracujuci p on pv.pracujuci = p.id join pozicia po on ppv.pozicia = po.id join pracovisko pr on po.pracovisko = pr.id where '".$date."' between n.od and n.do group by n.id order by p_id";
			$query = $this->db->query($command);
		}

		return $query->result();
	}

	public function get_absences_by_monthid($month_id)
	{

		$command = "select * from odpracovany_mesiac_nepritomnost omn join nepritomnost n on omn.nepritomnost = n.id where omn.odpracovany_mesiac=".$month_id;
		$query = $this->db->query($command);

		return $query->result();
	}


	public function create_absence()
	{
		$params = $_REQUEST;
		$bsnc_id = null;

		if($params['from']=='NULL')
			$bsnc_from=null;
		else
			$bsnc_from = $params['from'];
		if($params['to']=='NULL')
			$bsnc_to=null;
		else
			$bsnc_to = $params['to'];
		if($params['half']=='NULL')
			$bsnc_half=null;
		else
			$bsnc_half = $params['half'];
		if($params['type']=='NULL')
			$bsnc_type=null;
		else
			$bsnc_type = $params['type'];
		if($params['char']=='NULL')
			$bsnc_char=null;
		else
			$bsnc_char = $params['char'];

		$cons_id = $params['consid'];


		$query = $this->db->query("select * from podmienky_pracovneho_vztahu ppv where ppv.id = " . $cons_id );
		$result = $query->result_array();
		$cons_from = $result[0]["platnost_od"];
		if($cons_from>$bsnc_from)
			return array('status' => 403, 'message' => 'Obdobie je mimo obdobia podmienok pracovneho vztahu.');

		$start    =new DateTime($bsnc_from);
		$start->modify('first day of this month');
		$end      = new DateTime($bsnc_to);
		$end->modify('last day of this month');
		$interval = DateInterval::createFromDateString('1 month');
		$months   = new DatePeriod($start, $interval, $end);

		$this->db->trans_start();

			$month_ids = array();
			foreach ($months as $dt)
		    {
				$month = intval($dt->format("m"));
				$year = intval($dt->format("Y"));

				$query = $this->db->query("select om.* from podmienky_pracovneho_vztahu ppv join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu join odpracovany_mesiac om on o.id = om.odpracovany_rok where ppv.id = " . $cons_id . " and o.rok= " . $year . " and om.poradie_mesiaca= " . $month);
				$rows_count = $query->num_rows();

				if ($rows_count == 0)
				{
					$query = $this->db->query("select o.* from podmienky_pracovneho_vztahu ppv join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu where ppv.id = " . $cons_id . " and o.rok= " . $year);
					$rows_count = $query->num_rows();
					if ($rows_count == 0) {
						$data = array(
							'rok' => $year,
							'podmienky_pracovneho_vztahu' => $cons_id
						);
						$this->db->insert('odpracovany_rok', $data);
						$year_id = $this->db->insert_id();
					} else if ($rows_count == 1) {
						$result = $query->result_array();
						$year_id = $result[0]["id"];
					} else {
						return array('status' => 403, 'message' => 'Existuje viacero možných rokov.');
					}

					$data = array(
						'poradie_mesiaca' => $month,
						'odpracovany_rok' => $year_id
					);
					$this->db->insert('odpracovany_mesiac', $data);
					$month_id = $this->db->insert_id();
					array_push($month_ids, $month_id);
				}
				else if ($rows_count == 1)
				{
					$result = $query->result_array();
					$month_id = $result[0]["id"];
					$month_closed = $result[0]["je_mesiac_uzatvoreny"];
					if ($month_closed == 1)
						return array('status' => 403, 'message' => 'Mesiac je uz uzatvoreny. Nie je mozne pridavat nepritomnost.');
					array_push($month_ids, $month_id);
				}
				else
				{
					return array('status' => 403, 'message' => 'Existuje viacero možných mesiacov.');
				}
			}

			$data = array(
				'od' => $bsnc_from,
				'do' => $bsnc_to,
				'je_polovica_dna' => $bsnc_half,
				'typ_dovodu' => $bsnc_type,
				'popis_dovodu' => $bsnc_char
			);
			$this->db->insert('nepritomnost', $data);
			$bsnc_id = $this->db->insert_id();

			foreach ($month_ids as $id)
			{
				$data = array(
					'odpracovany_mesiac' => $id,
					'nepritomnost' => $bsnc_id
				);
				$this->db->insert('odpracovany_mesiac_nepritomnost', $data);
			}

		if($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'hrs_id' => $bsnc_id);
		}
	}

	public function create_feast()
	{
		$params = $_REQUEST;
		$bsnc_id = null;

		$bsnc_from = $params['from'];
		$bsnc_to = $params['to'];
		$bsnc_half = $params['half'];
		$bsnc_type = $params['type'];
		$bsnc_char = $params['char'];
		$bsnc_num = intval($params['num']);

		$cons_ids = array();
		for ($i=0; $i<$bsnc_num; $i++)
		{
			 array_push($cons_ids, $params[('consid'.$i)]);
		}

		$month = intval(date("m",strtotime($bsnc_from)));
		$year = intval(date("Y",strtotime($bsnc_from)));

		$this->db->trans_start();

			foreach ($cons_ids as $cons_id)
		    {
				$query = $this->db->query("select * from podmienky_pracovneho_vztahu ppv where ppv.id = " . $cons_id );
				$result = $query->result_array();
				$cons_from = $result[0]["platnost_od"];
				if($cons_from>$bsnc_from)
					return array('status' => 403, 'message' => 'Obdobie je mimo obdobia podmienok pracovneho vztahu.');

				$data = array(
					'od' => $bsnc_from,
					'do' => $bsnc_to,
					'je_polovica_dna' => $bsnc_half,
					'typ_dovodu' => $bsnc_type,
					'popis_dovodu' => $bsnc_char
				);
				$this->db->insert('nepritomnost', $data);
				$bsnc_id = $this->db->insert_id();

				$query = $this->db->query("select om.* from podmienky_pracovneho_vztahu ppv join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu join odpracovany_mesiac om on o.id = om.odpracovany_rok where ppv.id = " . $cons_id . " and o.rok= " . $year . " and om.poradie_mesiaca= " . $month);
				$rows_count = $query->num_rows();
				if ($rows_count == 0)
				{
					$query = $this->db->query("select o.* from podmienky_pracovneho_vztahu ppv join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu where ppv.id = " . $cons_id . " and o.rok= " . $year);
					$rows_count = $query->num_rows();
					if ($rows_count == 0) {
						$data = array(
							'rok' => $year,
							'podmienky_pracovneho_vztahu' => $cons_id
						);
						$this->db->insert('odpracovany_rok', $data);
						$year_id = $this->db->insert_id();
					} else if ($rows_count == 1) {
						$result = $query->result_array();
						$year_id = $result[0]["id"];
					} else {
						return array('status' => 403, 'message' => 'Existuje viacero možných rokov.');
					}

					$data = array(
						'poradie_mesiaca' => $month,
						'odpracovany_rok' => $year_id
					);
					$this->db->insert('odpracovany_mesiac', $data);
					$month_id = $this->db->insert_id();
				}
				else if ($rows_count == 1)
				{
					$result = $query->result_array();
					$month_id = $result[0]["id"];
					$month_closed = $result[0]["je_mesiac_uzatvoreny"];
					if ($month_closed == 1)
						return array('status' => 403, 'message' => 'Mesiac je uz uzatvoreny. Nie je mozne pridavat nepritomnost.');
				}
				else
				{
					return array('status' => 403, 'message' => 'Existuje viacero možných mesiacov.');
				}

				$data = array(
					'odpracovany_mesiac' => $month_id,
					'nepritomnost' => $bsnc_id
				);
				$this->db->insert('odpracovany_mesiac_nepritomnost', $data);
			}
			
		if($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!');
		}
	}

	public function update_absence()
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

	public function delete_absence($id)
	{
		$this->db->trans_start();

			$this->db->where('nepritomnost', $id)->delete('odpracovany_mesiac_nepritomnost');
			$this->db->where('id', $id)->delete('nepritomnost');

		if($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'id' => $id);
		}
	}
}

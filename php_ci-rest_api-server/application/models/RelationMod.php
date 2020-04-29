<?php


class RelationMod extends CI_Model
{
	public function get_relation($id)
	{
		$query = $this->db->query("select *,  DATE_FORMAT(v.datum_vzniku,'%d.%m.%Y') as nice_date1,  DATE_FORMAT(v.datum_vyprsania,'%d.%m.%Y') as nice_date2 from pracovny_vztah v where v.id=".$id);
		return $query->result();
	}

	public function get_con_ncon_po_pl($id)
	{
		$query = $this->db->query("select dp.*, ppv.*, dp.id as dp_id, ppv.id as ppv_id, pr.nazov as pr_nazov, po.nazov as po_nazov from dalsie_podmienky dp right outer join podmienky_pracovneho_vztahu ppv on dp.id = ppv.dalsie_podmienky join pozicia po on ppv.pozicia = po.id join pracovisko pr on po.pracovisko = pr.id where ppv.pracovny_vztah= ".$id." order by ppv.platnost_od");
		return $query->result();
	}

	public function get_emp_rel_cons_po_pl()
	{
		$query = $this->db->query("select p.id as p_id, p.meno as p_meno, p.priezvisko as p_priezvisko, pv.*,  DATE_FORMAT(pv.datum_vzniku,'%d.%m.%Y') as nice_date1,  DATE_FORMAT(pv.datum_vyprsania,'%d.%m.%Y') as nice_date2, ppv.id as ppv_id,  DATE_FORMAT(ppv.platnost_od,'%d.%m.%Y')  as ppv_platnost_od,  DATE_FORMAT(ppv.platnost_do,'%d.%m.%Y') as ppv_platnost_do, po.id as po_id, po.nazov as po_nazov, pr.id as pr_id, pr.nazov as pr_nazov from pracujuci p join pracovny_vztah pv on p.id = pv.pracujuci join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah join pozicia po on ppv.pozicia = po.id join pracovisko pr on po.pracovisko = pr.id where now() >= pv.datum_vzniku and (now() <= DATE_ADD(pv.datum_vyprsania, INTERVAL 1 MONTH) or pv.datum_vyprsania is null) and now() >= ppv.platnost_od and (now() <= DATE_ADD(ppv.platnost_do, INTERVAL 1 MONTH) or ppv.platnost_do is null) order by p.id");
		return $query->result();
	}

	public function get_next_demands_detail($id)
	{
		$query = $this->db->query("select * from dalsie_podmienky dup where dup.id=".$id);
		return $query->result();
	}

	public function get_conditions_of_relation($id)
	{
		$query = $this->db->query("select * from podmienky_pracovneho_vztahu where pracovny_vztah = ".$id);
		return $query->result_array();
	}

	public function get_relation_by_conditions($con_id)
	{
		$query = $this->db->query("select p.id as p_id, p.meno as p_meno, p.priezvisko as p_priezvisko, pv.*,  DATE_FORMAT(pv.datum_vzniku,'%d.%m.%Y') as nice_date1,  DATE_FORMAT(pv.datum_vyprsania,'%d.%m.%Y') as nice_date2, ppv.id as ppv_id, ppv.platnost_od as ppv_platnost_od, ppv.platnost_do as ppv_platnost_do, po.id as po_id, po.nazov as po_nazov, pr.id as pr_id, pr.nazov as pr_nazov from pracujuci p join pracovny_vztah pv on p.id = pv.pracujuci join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah join pozicia po on ppv.pozicia = po.id join pracovisko pr on po.pracovisko = pr.id where ppv.id= ".$con_id);
		return $query->result();
	}


	public function create_relation()
	{
		$params = $_REQUEST;

		$rel_id = null;
		$rel_type = $params['type'];
		$rel_begin = $params['begin'];
		$rel_end = assign_value($params['end']);
		$rel_employee_id = $params['employeeid'];

		$nextcon_id = null;
		$nextcon_bool = $params['nextconditions'];
		$nextcon_main= null;
		$nextcon_holliday = null;
		$nextcon_weektime = null;
		$nextcon_uniform = null;
		$nextcon_testtime = null;
		$nextcon_sacktime = null;

		$nextcon_daytime = null;
		$nextcon_apweektime = null;
		$nextcon_deductableitem = null;

		if($nextcon_bool=="true")
		{
			$nextcon_main = $params['main'];
			$nextcon_holliday = $params['holliday'];
			$nextcon_weektime = $params['weektime'];
			$nextcon_uniform = $params['uniform'];
			$nextcon_testtime = $params['testtime'];
			$nextcon_sacktime = $params['sacktime'];

			$nextcon_daytime = assign_value($params['daytime']);
			$nextcon_apweektime = assign_value($params['apweektime']);
			$nextcon_deductableitem = assign_value($params['deductableitem']);
		}

		$con_id = null;
		$con_from = $params['from'];
		$con_to = assign_value($params['to']);
		$con_rel_id = null;
		$con_position_id = $params['positionid'];
		$con_nextcon_id = null;

		$con_nextcon_taxfree = assign_value($params['taxfree']);
		$con_nextcon_taxbonus = assign_value($params['taxbonus']);
		$con_nextcon_disabled = assign_value($params['disabled']);
		$con_nextcon_retirement = assign_value($params['retirement']);
		$con_nextcon_invalidity40 = assign_value($params['invalidity40']);
		$con_nextcon_invalidity70 = assign_value($params['invalidity70']);
		$con_nextcon_premature = assign_value($params['premature']);
		$con_nextcon_exemption = assign_value($params['exemption']);
		$con_nextcon_bank = assign_value($params['bank']);
		$con_nextcon_bankpart = assign_value($params['bankpart']);
		$con_nextcon_iban = assign_value($params['iban']);


		$this->db->trans_start();

			$data = array(
				'typ' => $rel_type,
				'datum_vzniku' => $rel_begin,
				'datum_vyprsania' => $rel_end,
				'pracujuci' => $rel_employee_id
			);
			$this->db->insert('pracovny_vztah', $data);
			$rel_id = $this->db->insert_id();
			$con_rel_id = $rel_id;

			if($nextcon_bool=="true")
			{
				$data = array(
					'je_hlavny_pp' => $nextcon_main,
					'vymera_dovolenky' => $nextcon_holliday,
					'dohodnuty_tyzdenny_pracovny_cas' => $nextcon_weektime,
					'je_pracovny_cas_rovnomerny' => $nextcon_uniform,
					'skusobvna_doba' => $nextcon_testtime,
					'vypovedna_doba' => $nextcon_sacktime,

					'ustanoveny_tyzdenny_pracovny_cas' => $nextcon_apweektime,
					'dohodnuty_denny_pracovny_cas' => $nextcon_daytime,
					'uplatnenie_odpocitatelnej_polozky' => $nextcon_deductableitem
				);
				$this->db->insert('dalsie_podmienky', $data);
				$nextcon_id = $this->db->insert_id();
				$con_nextcon_id = $nextcon_id;
			}else{
				$nextcon_id = -1;
				$con_nextcon_id = null;
			}


			$data = array(
				'platnost_od' => $con_from,
				'platnost_do' => $con_to,
				'pracovny_vztah' => $con_rel_id,
				'pozicia' => $con_position_id,
				'dalsie_podmienky' =>$con_nextcon_id,

				'uplatnenie_nezdanitelnej_casti' =>$con_nextcon_taxfree,
				'uplatnenie_danoveho_bonusu' =>$con_nextcon_taxbonus,
				'drzitel_tzp_preukazu' =>$con_nextcon_disabled,
				'poberatel_starobneho_vysluhoveho_dochodku' =>$con_nextcon_retirement,
				'poberatel_invalidneho_vysluhoveho_dochodku_nad_40' =>$con_nextcon_invalidity40,
				'poberatel_invalidneho_vysluhoveho_dochodku_nad_70' =>$con_nextcon_invalidity70,
				'poberatel_predcasneho_dochodku' =>$con_nextcon_premature,
				'uplatnenie_odvodovej_vynimky' =>$con_nextcon_exemption,
				'posielanie_vyplaty_na_ucet' =>$con_nextcon_bank,
				'cast_z_vyplaty_na_ucet' =>$con_nextcon_bankpart,
				'iban_uctu_pre_vyplatu' =>$con_nextcon_iban
			);
			$this->db->insert('podmienky_pracovneho_vztahu', $data);
			$con_id = $this->db->insert_id();

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'rel_id' => $rel_id, 'nextcon_id' => $nextcon_id, 'con_id' => $con_id);
		}
	}

	public function update_relation()
	{
		$params = $_REQUEST;

		$nextcon_id = null;
		$nextcon_bool = $params['nextconditions'];
		$nextcon_main= null;
		$nextcon_holliday = null;
		$nextcon_weektime = null;
		$nextcon_uniform = null;
		$nextcon_testtime = null;
		$nextcon_sacktime = null;

		$nextcon_daytime = null;
		$nextcon_apweektime = null;
		$nextcon_deductableitem = null;

		if($nextcon_bool=="true")
		{
			$nextcon_main = $params['main'];
			$nextcon_holliday = $params['holliday'];
			$nextcon_weektime = $params['weektime'];
			$nextcon_uniform = $params['uniform'];
			$nextcon_testtime = $params['testtime'];
			$nextcon_sacktime = $params['sacktime'];

			$nextcon_daytime = assign_value($params['daytime']);
			$nextcon_apweektime = assign_value($params['apweektime']);
			$nextcon_deductableitem = assign_value($params['deductableitem']);
		}

		$con_id = null;
		$con_from = $params['from'];
		$con_to = null;
		$con_rel_id = $params['relid'];
		$con_position_id = $params['positionid'];
		$con_nextcon_id = null;

		$con_nextcon_taxfree = assign_value($params['taxfree']);
		$con_nextcon_taxbonus = assign_value($params['taxbonus']);
		$con_nextcon_disabled = assign_value($params['disabled']);
		$con_nextcon_retirement = assign_value($params['retirement']);
		$con_nextcon_invalidity40 = assign_value($params['invalidity40']);
		$con_nextcon_invalidity70 = assign_value($params['invalidity70']);
		$con_nextcon_premature = assign_value($params['premature']);
		$con_nextcon_exemption = assign_value($params['exemption']);
		$con_nextcon_bank = assign_value($params['bank']);
		$con_nextcon_bankpart = assign_value($params['bankpart']);
		$con_nextcon_iban = assign_value($params['iban']);

		$prevcon_id = $params['prevconds'];
		$query = $this->db->query("select platnost_od from podmienky_pracovneho_vztahu where id = ".$prevcon_id);
		$prevcon_end = $query->row()->platnost_od;
		if(!($prevcon_end<$con_from))
			return array('status' => 403,'message' => 'Nie je mozne pridat podmientky starsie ako aktualne.');

		$prevcon_end = date_create($con_from)->modify('-1 days')->format('Y-m-d');


		$this->db->trans_start();

			$data = array(
				'platnost_do' => $prevcon_end
			);
			$this->db->where('id', $prevcon_id)->update('podmienky_pracovneho_vztahu', $data);

			if($nextcon_bool=="true")
			{
				$data = array(
					'je_hlavny_pp' => $nextcon_main,
					'vymera_dovolenky' => $nextcon_holliday,
					'dohodnuty_tyzdenny_pracovny_cas' => $nextcon_weektime,
					'je_pracovny_cas_rovnomerny' => $nextcon_uniform,
					'skusobvna_doba' => $nextcon_testtime,
					'vypovedna_doba' => $nextcon_sacktime,

					'ustanoveny_tyzdenny_pracovny_cas' => $nextcon_apweektime,
					'dohodnuty_denny_pracovny_cas' => $nextcon_daytime,
					'uplatnenie_odpocitatelnej_polozky' => $nextcon_deductableitem
				);
				$this->db->insert('dalsie_podmienky', $data);
				$nextcon_id = $this->db->insert_id();
				$con_nextcon_id = $nextcon_id;
			}else{
				$nextcon_id = 'NULL';
				$con_nextcon_id = null;
			}

			$data = array(
				'platnost_od' => $con_from,
				'platnost_do' => $con_to,
				'pracovny_vztah' => $con_rel_id,
				'pozicia' => $con_position_id,
				'dalsie_podmienky' =>$con_nextcon_id,

				'uplatnenie_nezdanitelnej_casti' =>$con_nextcon_taxfree,
				'uplatnenie_danoveho_bonusu' =>$con_nextcon_taxbonus,
				'drzitel_tzp_preukazu' =>$con_nextcon_disabled,
				'poberatel_starobneho_vysluhoveho_dochodku' =>$con_nextcon_retirement,
				'poberatel_invalidneho_vysluhoveho_dochodku_nad_40' =>$con_nextcon_invalidity40,
				'poberatel_invalidneho_vysluhoveho_dochodku_nad_70' =>$con_nextcon_invalidity70,
				'poberatel_predcasneho_dochodku' =>$con_nextcon_premature,
				'uplatnenie_odvodovej_vynimky' =>$con_nextcon_exemption,
				'posielanie_vyplaty_na_ucet' =>$con_nextcon_bank,
				'cast_z_vyplaty_na_ucet' =>$con_nextcon_bankpart,
				'iban_uctu_pre_vyplatu' =>$con_nextcon_iban
			);
			$this->db->insert('podmienky_pracovneho_vztahu', $data);
			$con_id = $this->db->insert_id();

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!',  'nextcon_id' => $nextcon_id, 'con_id' => $con_id);
		}
	}

	public function delete_relation($id)
	{
		$this->db->trans_start();

			$wage_array = $this->get_conditions_of_relation($id);
			foreach ($wage_array as $row)
			{
				$this->delete_conditions($row['id']);
			}
			$this->db->where('id',$id)->delete('pracovny_vztah');

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'id' => $id);
		}
	}

	public function delete_conditions($id)
	{
		$this->db->trans_start();

			$wage_array = $this->WageMod->get_wage_by_conditions($id);
			foreach ($wage_array as $row)
			{
				$this->db->where('id',$row['id'])->delete('zakladna_mzda');
			}

			$query = $this->db->query("select dalsie_podmienky from podmienky_pracovneho_vztahu where id = ".$id);
			$row = $query->row();
			$nconid =  $row->dalsie_podmienky;

			$this->db->where('id',$id)->delete('podmienky_pracovneho_vztahu');

			$this->db->where('id',$nconid)->delete('dalsie_podmienky');

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'nconid' => $nconid);
		}
	}

	public function delete_last_conditions($id, $prev_id)
	{
		$query = $this->db->query("select pracovny_vztah from podmienky_pracovneho_vztahu where id = ".$id);
		$relid =  $query->row()->pracovny_vztah;

		$query = $this->db->query("select count(id) as count from podmienky_pracovneho_vztahu where pracovny_vztah = ".$relid);
		$count =  $query->row()->count;
		if($count<2)
		{
			return array('status' => 403,'message' => 'Nie je mozne odstranit posledne ostavajuce podmienky vztahu!');
		}

		$this->db->trans_start();

			$wage_array = $this->WageMod->get_wage_by_conditions($id);
			foreach ($wage_array as $row)
			{
				$this->db->where('id',$row['id'])->delete('zakladna_mzda');
			}

			$query = $this->db->query("select dalsie_podmienky from podmienky_pracovneho_vztahu where id = ".$id);
			$row = $query->row();
			$nconid =  $row->dalsie_podmienky;

			$this->db->where('id',$id)->delete('podmienky_pracovneho_vztahu');

			$this->db->where('id',$nconid)->delete('dalsie_podmienky');

			$data = array(
				'platnost_do' => null
			);
			$this->db->where('id', $prev_id)->update('podmienky_pracovneho_vztahu', $data);


		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'nconid' => $nconid);
		}
	}




}

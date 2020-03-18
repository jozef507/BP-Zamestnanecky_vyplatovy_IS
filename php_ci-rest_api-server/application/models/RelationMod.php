<?php


class RelationMod extends CI_Model
{
	public function get_relation($id)
	{
		$query = $this->db->query("select * from pracovny_vztah v where v.id=".$id);
		return $query->result();
	}

	public function get_con_ncon_po_pl($id)
	{
		$query = $this->db->query("select dp.*, ppv.*, dp.id as dp_id, ppv.id as ppv_id, pr.nazov as pr_nazov, po.nazov as po_nazov from dalsie_podmienky dp right outer join podmienky_pracovneho_vztahu ppv on dp.id = ppv.dalsie_podmienky join pozicia po on ppv.pozicia = po.id join pracovisko pr on po.pracovisko = pr.id where ppv.pracovny_vztah= ".$id." order by ppv.platnost_od");
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


	public function create_relation()
	{
		$params = $_REQUEST;
		$rel_id = null;
		$rel_type = $params['type'];
		$rel_begin = $params['begin'];
		if($params['end']=='NULL')
			$rel_end = null;
		else
			$rel_end = $params['end'];
		$rel_employee_id = $params['employeeid'];

		$nextcon_id = null;
		$nextcon_bool = $params['nextconditions'];
		$nextcon_main= null;
		$nextcon_holliday = null;
		$nextcon_weektime = null;
		$nextcon_uniform = null;
		$nextcon_testtime = null;
		$nextcon_sacktime = null;

		if($nextcon_bool=="true")
		{
			$nextcon_main = $params['main'];
			$nextcon_holliday = $params['holliday'];
			$nextcon_weektime = $params['weektime'];
			$nextcon_uniform = $params['uniform'];
			$nextcon_testtime = $params['testtime'];
			$nextcon_sacktime = $params['sacktime'];
		}


		$con_id = null;
		$con_from = $params['from'];
		if($params['to']=='NULL')
			$con_to = null;
		else
			$con_to = $params['to'];
		$con_rel_id = null;
		$con_position_id = $params['positionid'];
		$con_nextcon_id = null;


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
					'tyzdenny_pracovny_cas' => $nextcon_weektime,
					'je_pracovny_cas_rovnomerny' => $nextcon_uniform,
					'skusobvna_doba' => $nextcon_testtime,
					'vypovedna_doba' => $nextcon_sacktime
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
				'dalsie_podmienky' =>$con_nextcon_id
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

		if($nextcon_bool=="true")
		{
			$nextcon_main = $params['main'];
			$nextcon_holliday = $params['holliday'];
			$nextcon_weektime = $params['weektime'];
			$nextcon_uniform = $params['uniform'];
			$nextcon_testtime = $params['testtime'];
			$nextcon_sacktime = $params['sacktime'];
		}

		$con_id = null;
		$con_from = $params['from'];
		$con_to = null;
		$con_rel_id = $params['relid'];
		$con_position_id = $params['positionid'];
		$con_nextcon_id = null;

		$prevcon_id = $params['prevconds'];
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
					'tyzdenny_pracovny_cas' => $nextcon_weektime,
					'je_pracovny_cas_rovnomerny' => $nextcon_uniform,
					'skusobvna_doba' => $nextcon_testtime,
					'vypovedna_doba' => $nextcon_sacktime
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
				'dalsie_podmienky' =>$con_nextcon_id
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

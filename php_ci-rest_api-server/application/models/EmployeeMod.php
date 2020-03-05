<?php


class EmployeeMod extends CI_Model
{
	public function get_all_employees()
	{
		$query = $this->db->query("select p.*, count(v.id) as c from pracujuci p left join pracovny_vztah v on p.id = v.pracujuci where (now() > v.datum_vzniku and (now() < v.datum_vyprsania or v.datum_vyprsania is null)) or v.pracujuci is null group by p.id");
		return $query->result();
	}

	public function get_employee_detail($id)
	{
		$query = $this->db->query("select * from pracujuci p join prihlasovacie_konto pk on p.prihlasovacie_konto=pk.id where p.id=".$id);
		return $query->result();
	}

	public function get_relations_places_details($id)
	{
		$query = $this->db->query("select v.typ, pr.nazov from pracujuci p join pracovny_vztah v join podmienky_pracovneho_vztahu ppv join pozicia po join pracovisko pr on p.id = v.pracujuci and v.id = ppv.pracovny_vztah and ppv.pozicia = po.id and po.pracovisko = pr.id where now() > v.datum_vzniku and (now() < v.datum_vyprsania or v.datum_vyprsania is null) and p.id = ".$id);
		return $query->result();
	}

	public function get_emp_usr_imp($id)
	{
		$query = $this->db->query("select pk.id as pk_id, pk.email, pk.typ_prav, p.id as p_id, p.*, du.id as du_id, du.*,  DATE_FORMAT(p.datum_narodenia,'%d.%m.%Y') as nice_date1, DATE_FORMAT(du.platnost_od,'%d.%m.%Y') as nice_date2 from prihlasovacie_konto pk join pracujuci p join dolezite_udaje_pracujuceho du on p.id = du.pracujuci and p.prihlasovacie_konto = pk.id where p.id = ".$id." and now() > du.platnost_od and (now() < du.platnost_do or du.platnost_do is null)");
		return $query->result();
	}

	public function get_emp_rel_ov($id)
	{
		$query = $this->db->query("select p.id, v.*, ppv.id, ppv.dalsie_podmienky, poz.nazov as poz_nazov, pr.nazov as pr_nazov,  DATE_FORMAT(v.datum_vzniku,'%d.%m.%Y') as nice_date1,  DATE_FORMAT(v.datum_vyprsania,'%d.%m.%Y') as nice_date2 from pracujuci p join pracovny_vztah v join podmienky_pracovneho_vztahu ppv join pozicia poz join pracovisko pr on p.id = v.pracujuci and v.id=ppv.pracovny_vztah and ppv.pozicia=poz.id and poz.pracovisko=pr.id where (now() > v.datum_vzniku and (now() < v.datum_vyprsania or v.datum_vyprsania is null)) and (now() > ppv.platnost_od and (now() < ppv.platnost_do or ppv.platnost_do is null)) and p.id =". $id);
		return $query->result();
	}

	public function get_count_imp_pay($id)
	{
		$query = $this->db->query("select count(vp.id) as c from dolezite_udaje_pracujuceho dup join vyplatna_paska vp on dup.id = vp.dolezite_udaje_pracujuceho where dup.id =". $id);
		return $query->result();
	}

	public function get_all_imp($id)
	{
		$query = $this->db->query("select dup.*,  DATE_FORMAT(dup.platnost_od,'%d.%m.%Y') as nice_date1, DATE_FORMAT(dup.platnost_do,'%d.%m.%Y') as nice_date2 from pracujuci p join dolezite_udaje_pracujuceho dup on p.id = dup.pracujuci where p.id= ".$id." order by dup.platnost_od");
		return $query->result();
	}

	public function update_employee()
	{
		$params = $_REQUEST;
		$id = $params['id'];
		$name = $params['name'];
		$lastname = $params['lastname'];
		$phone = $params['phone'];
		$bornnumber = $params['bornnumber'];
		$borndate = $params['borndate'];

		$this->db->trans_start();

		$data = array(
			'meno' => $name,
			'priezvisko' => $lastname,
			'telefon' => $phone,
			'rodne_cislo' => $bornnumber,
			'datum_narodenia' => $borndate,
		);
		$this->db->where('id', $id);
		$this->db->update('pracujuci', $data);

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'id' => $id);
		}
	}


	public function update_employee_important()
	{
		$params = $_REQUEST;
		$id = $params['id'];
		$inscomp = $params['inscomp'];
		$town = $params['town'];
		$street = $params['street'];
		$num = $params['num'];
		$childunder = $params['childunder'];
		$childover = $params['childover'];
		$part = $params['part'];
		$retirement = $params['retirement'];
		$invalidity = $params['invalidity'];
		$end = $params['end'];
		$begin = $params['begin'];



		$this->db->trans_start();

		$this->db->select('platnost_od, pracujuci');
		$this->db->from('dolezite_udaje_pracujuceho');
		$this->db->where('id', $id );
		$query = $this->db->get();
		$row = $query->row_array();
		$p_od = $row['platnost_od'];
		$id_pracujuci = $row['pracujuci'];

		$int = date('Y-m-d', strtotime('-2 months'));
		if(!($begin>$int && $begin>$p_od))
		{
			$this->db->trans_rollback();
			return array('status' => 403,'message' => 'Zadany nespravny datum!');
		}

		$data = array(
			'platnost_do' => $end
		);
		$this->db->where('id', $id);
		$this->db->update('dolezite_udaje_pracujuceho', $data);

		$data = array(
			'zdravotna_poistovna' => $inscomp,
			'mesto' => $town,
			'ulica' => $street,
			'cislo' => $num,
			'pocet_deti_do_6_rokov' => $childunder,
			'pocet_deti_nad_6_rokov' => $childover,
			'uplatnenie_nedzanitelnej_casti' => $part,
			'poberatel_starobneho_dochodku' => $retirement,
			'poberatel_invalidneho_dochodku' => $invalidity,
			'platnost_od' => $begin,
			'platnost_do' => null,
			'pracujuci' => $id_pracujuci
		);
		$this->db->insert('dolezite_udaje_pracujuceho', $data);
		$new_id = $this->db->insert_id();


		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'id' => $new_id);
		}
	}

	public function delete_employee_important($id)
	{
		$this->db->trans_start();

		$query = $this->db->query("select pracujuci from dolezite_udaje_pracujuceho  where id = ".$id);
		$pid = $query->row()->pracujuci;

		$this->db->where('id', $id);
		$this->db->delete('dolezite_udaje_pracujuceho');

		$query = $this->db->query("select dup.id from pracujuci p join dolezite_udaje_pracujuceho dup on p.id = dup.pracujuci where dup.platnost_do is not null and p.id = ".$pid." order by dup.platnost_od desc limit 1");
		$row = $query->row();
		$nid =  $row->id;
		$data = array(
			'platnost_do' => null
		);
		$this->db->where('id', $nid);
		$this->db->update('dolezite_udaje_pracujuceho', $data);

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'id' => $id);
		}
	}
}

<?php


class EmployeeMod extends CI_Model
{
	public function get_all_employees()
	{
		$query = $this->db->query("select p.*,  v.typ as p2_nazov, p3.nazov as p3_nazov from pracovisko p3 join pozicia p2 on p2.pracovisko = p3.id join podmienky_pracovneho_vztahu ppv on ppv.pozicia = p2.id join pracovny_vztah v on v.id = ppv.pracovny_vztah right join pracujuci p on  p.id = v.pracujuci order by p.id");
		return $query->result();
	}

	public function get_emp_usr($id)
	{
		$query = $this->db->query("select *, p.id as p_id, pk.id as pk_id from pracujuci p left join prihlasovacie_konto pk on p.prihlasovacie_konto = pk.id where p.id = ".$id);
		return $query->result();
	}

	public function get_emps_usrs()
	{
		$query = $this->db->query("select *, p.id as p_id, pk.id as pk_id from pracujuci p left join prihlasovacie_konto pk on p.prihlasovacie_konto = pk.id");
		return $query->result();
	}

	public function get_employee_detail($id)
	{
		$query = $this->db->query("select * from pracujuci p join prihlasovacie_konto pk on p.prihlasovacie_konto=pk.id where p.id=".$id);
		return $query->result();
	}

	public function get_relations_places_details($id)
	{
		$query = $this->db->query("select v.typ, pr.nazov from pracujuci p join pracovny_vztah v join podmienky_pracovneho_vztahu ppv join pozicia po join pracovisko pr on p.id = v.pracujuci and v.id = ppv.pracovny_vztah and ppv.pozicia = po.id and po.pracovisko = pr.id where now() > v.datum_vzniku and (now() < v.datum_vyprsania or v.datum_vyprsania is null or v.datum_vyprsania = '0000-00-00') and p.id = ".$id);
		return $query->result();
	}

	public function get_emp_usr_imp($id)
	{
		$query = $this->db->query("select pk.id as pk_id, pk.email, pk.typ_prav, p.id as p_id, p.*,  DATE_FORMAT(p.datum_narodenia,'%d.%m.%Y') as nice_date1 from prihlasovacie_konto pk right join pracujuci p  on p.prihlasovacie_konto = pk.id where p.id = ".$id);
		$result1 = $query->result();
		$query = $this->db->query("select du.id as du_id, du.*, DATE_FORMAT(du.platnost_od,'%d.%m.%Y') as nice_date2 from dolezite_udaje_pracujuceho du where du.pracujuci = ".$id."  and  ((now() > du.platnost_od and (now() < du.platnost_do or du.platnost_do is null)))");
		$result2 = $query->result();
		$result = array_merge($result1, $result2);
		return $result;
	}

	public function get_emp_rel_ov($id)
	{
		$query = $this->db->query("select p.id as p_id, v.*, v.id as v_id, ppv.id as ppv_id, ppv.dalsie_podmienky, poz.nazov as poz_nazov, pr.nazov as pr_nazov,  DATE_FORMAT(v.datum_vzniku,'%d.%m.%Y') as nice_date1,  DATE_FORMAT(v.datum_vyprsania,'%d.%m.%Y') as nice_date2 from pracujuci p join pracovny_vztah v join podmienky_pracovneho_vztahu ppv join pozicia poz join pracovisko pr on p.id = v.pracujuci and v.id=ppv.pracovny_vztah and ppv.pozicia=poz.id and poz.pracovisko=pr.id where  p.id = ". $id." order by v.datum_vzniku desc");
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

	public function create_employee()
	{
		$params = $_REQUEST;

		$name = $params['name'];
		$lastname = $params['lastname'];
		$phone = $params['phone'];
		$bornnumber = $params['bornnum'];
		$borndate = $params['borndate'];

		$inscomp = $params['inscomp'];
		$town = $params['town'];
		$street = $params['street'];
		$num = $params['num'];
		$childunder = $params['childunder'];
		$childover = $params['childover'];
		$part = $params['part'];
		$retirement = $params['retirement'];
		$invalidity = $params['invalidity'];
		$begin = $params['begin'];
		if($params['end']=='NULL')
			$end = null;
		else
			$end = $params['end'];

		$this->db->trans_start();

			$data = array(
				'meno' => $name,
				'priezvisko' => $lastname,
				'telefon' => $phone,
				'rodne_cislo' => $bornnumber,
				'datum_narodenia' => $borndate,
			);
			$this->db->insert('pracujuci', $data);
			$emp_id = $this->db->insert_id();

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
				'platnost_do' => $end,
				'pracujuci' => $emp_id
			);
			$this->db->insert('dolezite_udaje_pracujuceho', $data);
			$imp_id = $this->db->insert_id();

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'emp_id' => $emp_id, 'imp_id' => $imp_id);
		}
	}

	public function create_user()
	{
		$params = $_REQUEST;

		$emp_id = $params['employeeid'];
		$email = $params['email'];
		$password = $params['password'];
		$role = $params['role'];
		$date = date('Y-m-d H:i:s');

		$query = $this->db->query("select prihlasovacie_konto from pracujuci where id = ".$emp_id);
		$pid = $query->row()->prihlasovacie_konto;
		if($pid!=null)
			return array('status' => 403,'message' => 'Pracujuci uz prihlasovacie konto vlastni.');

		$this->db->trans_start();

			$data = array(
				'email' => $email,
				'heslo' => $password,
				'typ_prav' => $role,
				'vytvorene_v' => $date
			);
			$this->db->insert('prihlasovacie_konto', $data);
			$usr_id = $this->db->insert_id();

			$data = array(
				'prihlasovacie_konto' => $usr_id
			);
			$this->db->where('id', $emp_id)->update('pracujuci', $data);

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'usr_id' => $usr_id);
		}
	}

	public function update_user()
	{
		$params = $_REQUEST;

		$user_id = $params['userid'];
		$role = $params['role'];
		$current = $params['current'];
		$date = date('Y-m-d H:i:s');

		$this->db->trans_start();

			$data = array(
				'typ_prav' => $role,
				'aktualne' => $current,
				'aktualizovane_v' => $date
			);
			$this->db->where('id', $user_id)->update('prihlasovacie_konto', $data);

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'usr_id' => $user_id);
		}
	}

	public function update_user_password()
	{
		$params = $_REQUEST;

		$user_id = $params['userid'];
		$password = $params['password'];
		$date = date('Y-m-d H:i:s');

		$this->db->trans_start();

			$data = array(
				'heslo' => $password,
				'aktualizovane_v' => $date
			);
			$this->db->where('id', $user_id)->update('prihlasovacie_konto', $data);

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'usr_id' => $user_id);
		}
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

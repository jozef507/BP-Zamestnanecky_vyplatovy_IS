<?php


class MinWageMod extends CI_Model
{
	public function get_min_wages()
	{
		$query = $this->db->query("select mm.*,  DATE_FORMAT(mm.platnost_od,'%d.%m.%Y') as nice_date1, DATE_FORMAT(mm.platnost_do,'%d.%m.%Y') as nice_date2, sn.cislo_stupna from minimalna_mzda mm join stupen_narocnosti sn on mm.stupen_narocnosti=sn.id order by mm.platnost_od, mm.hodinova_hodnota");
		return $query->result();
	}

	public function create_min_wage()
	{
		$params = $_REQUEST;
		$minwage_id = null;
		$minwage_from = $params['from'];
		$minwage_to = null;
		$minwage_hourvalue = $params['hourvalue'];
		$minwage_monthvalue = $params['monthvalue'];
		$minwage_levelid = $params['levelid'];

		$query = $this->db->query("select * from minimalna_mzda where stupen_narocnosti = ".$minwage_levelid." order by platnost_od desc limit 1");
		if($query->num_rows() > 0) {// records to display
			$prev_to = $query->row()->platnost_do;
			if ($prev_to == null) {
				return array('status' => 403, 'message' => 'Vybrany stupen narocnosti obsahuje minimalnu mzdu ktora je stale aktualna!');
			} elseif (!($minwage_from > $prev_to)) {
				return array('status' => 403, 'message' => 'Platnost tejto novej minimalnej mzdy sa krizi s platnostou inej minimalnej mzdy tohto stupna narocnosti!');
			}
		}

		$this->db->trans_start();

			$data = array(
				'platnost_od' => $minwage_from,
				'platnost_do' => $minwage_to,
				'hodinova_hodnota' => $minwage_hourvalue,
				'mesacna_hodnota' => $minwage_monthvalue,
				'stupen_narocnosti' => $minwage_levelid
			);
			$this->db->insert('minimalna_mzda', $data);
			$minwage_id = $this->db->insert_id();

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'minwage_id' => $minwage_id);
		}
	}

	public function delete_min_wage($id)
	{
		$this->db->trans_start();

			$this->db->where('id', $id)->delete('minimalna_mzda');

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'id' => $id);
		}
	}

	public function update_min_wage()
	{
		$params = $_REQUEST;

		$minwage_id =  $params['id'];
		$minwage_to =  $params['to'];

		$this->db->trans_start();

		$data = array(
			'platnost_do' => $minwage_to
		);
		$this->db->where('id', $minwage_id)->update('minimalna_mzda', $data);

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!');
		}
	}




}

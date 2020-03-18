<?php


class LevelMod extends CI_Model
{
	public function get_levels()
	{
		$query = $this->db->query("select sn.id, sn.cislo_stupna, sn.charakteristika,  DATE_FORMAT(sn.platnost_od,'%d.%m.%Y') as nice_date1,  DATE_FORMAT(sn.platnost_do,'%d.%m.%Y') as nice_date2 from stupen_narocnosti sn");
		return $query->result();
	}

	public function create_level()
	{
		$params = $_REQUEST;

		$level_id = null;
		$level_num = $params['num'];
		$level_caracteristic = $params['caracteristic'];
		$level_from = $params['from'];
		$level_to = null;

		$query = $this->db->query("select * from stupen_narocnosti where cislo_stupna = ".$level_num." order by platnost_od desc limit 1");
		if($query->num_rows() > 0){// records to display
			$prev_to = $query->row()->platnost_do;
			if($prev_to==null)
			{
				return array('status' => 403,'message' => 'Existuje stupen narocnosti s tymto cislom stupna, ktory je aktualny! Najprv ukoncite platnost tohto existujuceho stupna narocnosti.');
			}
			elseif(!($level_from>$prev_to))
			{
				return array('status' => 403,'message' => 'Platnost tohto noveho stupna narocnosti sa krizi s platnostou ineho existujuceho podobného stupna narocnosti! Najprv ukoncite platnost tohto existujuceho stupna narocnosti.');
			}
		}


		$this->db->trans_start();

			$data = array(
				'cislo_stupna' => $level_num,
				'charakteristika' => $level_caracteristic,
				'platnost_od' => $level_from,
				'platnost_do' => $level_to
			);
			$this->db->insert('stupen_narocnosti', $data);
			$level_id = $this->db->insert_id();

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'level_id' => $level_id);
		}
	}

	public function update_level()
	{
		$params = $_REQUEST;

		$level_id =  $params['id'];
		$level_to =  $params['to'];

		$this->db->trans_start();

			$data = array(
				'platnost_do' => $level_to
			);
			$this->db->where('id', $level_id)->update('stupen_narocnosti', $data);

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!');
		}
	}

	public function delete_level($lvl_id)
	{
		$this->db->trans_start();

			$this->db->where('id', $lvl_id)->delete('stupen_narocnosti');

		if($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!');
		}
	}

}

<?php


class WageConstantsMod extends CI_Model
{
	public function get_constants()
	{
		$query = $this->db->query("select tp.*,  DATE_FORMAT(tp.platnost_od,'%d.%m.%Y') as nice_date1, DATE_FORMAT(tp.platnost_do,'%d.%m.%Y') as nice_date2 from mzdove_konstanty tp order by tp.platnost_od");
		return $query->result();
	}

	public function create_constants()
	{
		$params = $_REQUEST;
		$constants_id = null;
		$constants_to = null;


		$this->db->trans_start();

			$data = array(
				'platnost_od' => assign_value($params['from']),
				'platnost_do' => null,
				'zakladny_tyzdenny_pracovny_cas' => assign_value($params['basicWeekTime']),
				'max_vymeriavaci_zaklad' => assign_value($params['maxAssessmentBasis']),
				'min_vymeriavaci_zaklad' => assign_value($params['minAssessmentBasis']),
				'max_denny_vymeriavaci_zaklad' => assign_value($params['maxDayAssessmentBasis']),
				'danovy_bonus_na_dieta_nad_6' => assign_value($params['taxBonusOver6']),
				'danovy_bonus_na_dieta_pod_6' => assign_value($params['taxBonusUnder6']),
				'NCZD_na_danovnika' => assign_value($params['nonTaxablePart']),
				'nasobok_zivotneho_minima_pre_preddavok' => assign_value($params['subsistenceMinimumForAdvances']),
				'zaciatok_nocnej_prace' => assign_value($params['nightFrom']),
				'koniec_nocnej_prace' => assign_value($params['nightTo']),
				'max_vymeriavaci_zaklad_pre_OP' => assign_value($params['maxAssessmentBasisOP']),
				'max_vyska_OP' => assign_value($params['maxOP']),
				'hranica_prekrocenia_OV' => assign_value($params['limitOV'])
			);
			$this->db->insert('mzdove_konstanty', $data);
			$constants_id = $this->db->insert_id();

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'surcharge_id' => $constants_id);
		}
	}

	public function delete_constants($id)
	{
		$this->db->trans_start();

			$this->db->where('id', $id)->delete('mzdove_konstanty');

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'id' => $id);
		}
	}

	public function update_constants()
	{
		$params = $_REQUEST;

		$constants_id =  $params['id'];
		$constants_to =  $params['to'];

		$this->db->trans_start();

		$data = array(
			'platnost_do' => $constants_to
		);
		$this->db->where('id', $constants_id)->update('mzdove_konstanty', $data);

		if ($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!');
		}
	}
}

//
//  EvalPersonalTableViewCell.h
//  chengxin
//
//  Created by seniorcoder on 10/25/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface EvalPersonalTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *imgPhoto;
@property (weak, nonatomic) IBOutlet UILabel *userTypeLabel;
@property (weak, nonatomic) IBOutlet UILabel *lblRatio;
@property (weak, nonatomic) IBOutlet UILabel *lblEval;
@property (weak, nonatomic) IBOutlet UILabel *lblFrontEval;
@property (weak, nonatomic) IBOutlet UILabel *lblBackEval;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
@property (weak, nonatomic) IBOutlet UILabel *lblMobile;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *constrainLeftTitle;
@property (nonatomic, strong) IBOutlet UIButton *xyNameButton;

@end

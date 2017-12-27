//
//  MyEvalTableViewCell.h
//  chengxin
//
//  Created by common on 4/21/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface MyEvalTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIImageView *imgPhoto;
@property (weak, nonatomic) IBOutlet UIImageView *imgType;
@property (weak, nonatomic) IBOutlet UILabel *lblName;
@property (weak, nonatomic) IBOutlet UILabel *lblWriteTime;
@property (weak, nonatomic) IBOutlet UILabel *lblContent;
@property (weak, nonatomic) IBOutlet UILabel *lblEstimationType;
@property (weak, nonatomic) IBOutlet UILabel *lblElectCnt;
@property (weak, nonatomic) IBOutlet UILabel *lblEstimationCnt;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollThumb;
@end
